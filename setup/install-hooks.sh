#!/bin/bash

HOOK_DIR=".git/hooks"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HOOKS_SOURCE_DIR="$SCRIPT_DIR/git-hooks"

# Create hooks directory if it doesn't exist
mkdir -p $HOOK_DIR

# Copy all hooks from the source directory
for hook in "$HOOKS_SOURCE_DIR"/*; do
    if [ -f "$hook" ]; then
        hook_name=$(basename "$hook")
        echo "Installing $hook_name hook..."
        cp "$hook" "$HOOK_DIR/$hook_name"
        chmod +x "$HOOK_DIR/$hook_name"
    fi
done

echo "Git hooks installed successfully!"