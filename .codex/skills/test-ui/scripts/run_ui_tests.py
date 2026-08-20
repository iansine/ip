#!/usr/bin/env python3
"""Compile Sine and run the exact-output cases in the UI test plan."""

from __future__ import annotations

import argparse
import re
import subprocess
import sys
import tempfile
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class TestCase:
    """One independent console session from the Markdown test plan."""

    name: str
    aim: str
    inputs: str
    expected: str


def normalize_output(output: str) -> str:
    """Normalize platform line endings and insignificant final newlines."""
    return output.replace("\r\n", "\n").rstrip("\n") + "\n"


def load_cases(plan_path: Path) -> list[TestCase]:
    """Parse all test cases from the UI test plan."""
    plan = plan_path.read_text(encoding="utf-8")
    sections = re.split(r"^## Test case: ", plan, flags=re.MULTILINE)[1:]
    cases = []
    for section in sections:
        name, _, body = section.partition("\n")
        aim_match = re.search(r"^Aim: (.+)$", body, re.MULTILINE)
        inputs_match = re.search(
            r"^### Inputs\s+```text\n(.*?)^```$", body, re.MULTILINE | re.DOTALL
        )
        expected_match = re.search(
            r"^### Expected output\s+```text\n(.*?)^```$",
            body,
            re.MULTILINE | re.DOTALL,
        )
        if not (aim_match and inputs_match and expected_match):
            raise ValueError(f"Incomplete test case '{name}' in {plan_path}")
        cases.append(
            TestCase(
                name=name,
                aim=aim_match.group(1),
                inputs=inputs_match.group(1),
                expected=expected_match.group(1),
            )
        )
    if not cases:
        raise ValueError(f"No test cases found in {plan_path}")
    return cases


def print_block(label: str, content: str) -> None:
    """Print a labelled block while preserving its console formatting."""
    print(f"{label}:")
    print(content, end="" if content.endswith("\n") else "\n")


def main() -> int:
    """Compile the application, then run test cases until failure or completion."""
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--plan", default="test/ui-test-plan.md")
    parser.add_argument("--source-dir", default="src/main/java")
    parser.add_argument("--main-class", default="Sine")
    parser.add_argument("--java-home", required=True)
    args = parser.parse_args()

    plan_path = Path(args.plan)
    source_files = sorted(Path(args.source_dir).glob("*.java"))
    if not source_files:
        print(f"No Java source files found in {args.source_dir}", file=sys.stderr)
        return 2

    try:
        cases = load_cases(plan_path)
    except (OSError, ValueError) as error:
        print(error, file=sys.stderr)
        return 2

    java_home = Path(args.java_home)
    javac = java_home / "bin" / "javac"
    java = java_home / "bin" / "java"

    with tempfile.TemporaryDirectory(prefix="sine-ui-tests-") as class_dir:
        compile_result = subprocess.run(
            [str(javac), "-d", class_dir, *map(str, source_files)],
            text=True,
            capture_output=True,
            check=False,
        )
        if compile_result.returncode != 0:
            print("Compilation failed; no UI tests were run.", file=sys.stderr)
            print(compile_result.stdout, end="", file=sys.stderr)
            print(compile_result.stderr, end="", file=sys.stderr)
            return 2

        for position, case in enumerate(cases, start=1):
            inputs = normalize_output(case.inputs)
            expected = normalize_output(case.expected)
            result = subprocess.run(
                [str(java), "-cp", class_dir, args.main_class],
                input=inputs,
                text=True,
                capture_output=True,
                check=False,
            )
            actual = normalize_output(result.stdout)

            print(f"=== Test case {position}: {case.name} ===")
            print(f"Aim: {case.aim}")
            print_block("Console input", inputs)
            print_block("Console output", actual)

            if result.returncode != 0 or actual != expected:
                print("RESULT: FAIL")
                print_block("Expected output", expected)
                print_block("Actual output", actual)
                if result.stderr:
                    print_block("Standard error", result.stderr)
                return 1

            print("RESULT: PASS")

    print(f"All {len(cases)} UI test case(s) passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
