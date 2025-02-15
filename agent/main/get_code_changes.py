#!/usr/bin/env python3
import argparse
import os
import subprocess
import sys


def get_git_changes(folder):
    try:
        # Change to the specified folder
        os.chdir(folder)

        # Check if the specified folder is a git repository
        subprocess.run(["git", "status"], check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        # Get the full changes since the last commit
        changes = subprocess.check_output(["git", "diff", "--cached"], text=True).strip()

        if changes:
            return f"Changes since the last commit:\n\n {changes}"
        else:
            return None

    except subprocess.CalledProcessError:
        return "An error occurred while trying to get the last changes"
    except Exception as e:
        return "An error occurred while trying to get the last changes"


class CodeChangesProvider:
    def __init__(self, folder_path):
        self.folder_path = folder_path
        self.output = None

    def run(self):
        self.output = get_git_changes(self.folder_path)


def main():
    parser = argparse.ArgumentParser(description='Get git changes from a specified folder')
    parser.add_argument('folder', nargs='?', default=os.getcwd(),
                        help='The folder to check for git changes (defaults to current directory)')
    parser.add_argument('-q', '--quiet', action='store_true',
                        help='Only output changes, no error messages')

    args = parser.parse_args()

    provider = CodeChangesProvider(args.folder)
    provider.run()

    if provider.output:
        print(provider.output)
        sys.exit(0)
    elif not args.quiet:
        print("No staged changes found")
        sys.exit(1)
    else:
        sys.exit(1)


if __name__ == '__main__':
    main()
