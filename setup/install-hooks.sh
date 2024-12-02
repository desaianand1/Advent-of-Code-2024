#!/bin/bash

HOOK_DIR=".git/hooks"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Create hooks directory if it doesn't exist
mkdir -p $HOOK_DIR

# Create the pre-commit hook
echo "Installing pre-commit hook..."
cp "$SCRIPT_DIR/../.git/hooks/pre-commit" "$HOOK_DIR/pre-commit"
chmod +x "$HOOK_DIR/pre-commit"

echo "Git hooks installed successfully!"