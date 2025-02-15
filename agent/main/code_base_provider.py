#!/usr/bin/env python3
import argparse
import fnmatch
import os
import sys


def read_gitignore(ignore_path):
    """Reads the .gitignore file and returns a list of patterns to ignore."""
    gitignore_path = os.path.join(ignore_path, '.agentignore')
    ignore_patterns = []
    if os.path.exists(gitignore_path):
        with open(gitignore_path, 'r') as gitignore_file:
            ignore_patterns = [line.strip() for line in gitignore_file if line.strip() and not line.startswith('#')]
    return ignore_patterns

def is_ignored(path, ignore_patterns, folder_path):
    """Checks if a given path matches any .gitignore pattern."""
    rel_path = os.path.relpath(path, folder_path)
    for pattern in ignore_patterns:
        if fnmatch.fnmatch(rel_path, pattern) or fnmatch.fnmatch(rel_path, os.path.join(pattern, '*')):
            return True
    return False

def process_folder(folder_path, ignore_path):
    """Processes files in a folder, respecting .gitignore, and returns formatted content."""
    result = []
    ignore_patterns = read_gitignore(ignore_path)

    for root, _, files in os.walk(folder_path):
        for file_name in files:
            file_path = os.path.join(root, file_name)
            if is_ignored(file_path, ignore_patterns, folder_path):
                continue

            try:
                with open(file_path, 'r', encoding='utf-8') as file:
                    content = file.read()
            except (UnicodeDecodeError, FileNotFoundError):
                content = "[Error reading file]"

            relative_path = os.path.relpath(file_path, folder_path)
            result.append(f"//----------file-starts--------\n//{relative_path}:\n{content}\n//-----------file-ends---------\n\n")

    return "\n".join(result)

class CodeBaseProvider:
    def __init__(self, folder_path, ignore_path):
        self.folder_path = folder_path
        self.ignore_path = ignore_path
        self.output = None

    def run(self):
        self.output = process_folder(self.folder_path, self.ignore_path)


def main():
    parser = argparse.ArgumentParser(
        description='Process files in a folder while respecting .agentignore patterns')
    parser.add_argument('folder', nargs='?', default=os.getcwd(),
                        help='The folder to process (defaults to current directory)')
    parser.add_argument('-i', '--ignore-path', default=None,
                        help='Path to the directory containing .agentignore file (defaults to folder path)')
    parser.add_argument('-o', '--output', type=str,
                        help='Output file path (if not specified, prints to stdout)')
    parser.add_argument('-q', '--quiet', action='store_true',
                        help='Suppress error messages and warnings')

    args = parser.parse_args()

    # If ignore_path isn't specified, use the folder path
    ignore_path = args.ignore_path if args.ignore_path else args.folder

    try:
        provider = CodeBaseProvider(args.folder, ignore_path)
        provider.run()

        if not provider.output and not args.quiet:
            print("No files processed", file=sys.stderr)
            sys.exit(1)

        if args.output:
            with open(args.output, 'w', encoding='utf-8') as f:
                f.write(provider.output)
            if not args.quiet:
                print(f"Output written to {args.output}", file=sys.stderr)
        else:
            print(provider.output)

        sys.exit(0)

    except Exception as e:
        if not args.quiet:
            print(f"Error: {str(e)}", file=sys.stderr)
        sys.exit(1)


if __name__ == '__main__':
    main()
