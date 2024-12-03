#!/bin/bash

# Default to 2024 if no year provided
selected_year=${1:-2024}
if ! [[ $selected_year =~ ^[0-9]{4}$ ]]; then
    echo "Error: Invalid year format. Please provide a 4-digit year (yyyy)."
    exit 1
fi

# If no day provided, use current day if it's during AoC (Dec 1-25)
if [ -z "$2" ]; then
    current_month=$(date +%-m)
    current_day=$(date +%-d)

    if [ "$current_month" -eq 12 ] && [ "$current_day" -le 25 ]; then
        selected_day=$current_day
    else
        echo "Error: No day provided and current date is not during Advent of Code (December 1-25)."
        echo "Please provide a day number between 1 and 25."
        exit 1
    fi
else
    selected_day=$2
fi

if ! [[ $selected_day =~ ^[1-9]$|^1[0-9]$|^2[0-5]$ ]]; then
    echo "Error: Invalid day format. Please provide a valid day (1-25)."
    exit 1
fi

# Pad day with leading zero if necessary
padded_day=$(printf "%02d" "$selected_day")

function new_day {
    local year=$1
    local day=$2
    local padded_day=$(printf "%02d" "$day")
    local domain="adventofcode.com"
    local input_url="https://$domain/$year/day/$day/input"

    # Create directory structure
    local day_dir="src/main/kotlin/$year/day$padded_day"
    local test_dir="src/test/kotlin/$year/day$padded_day"
    local templates_dir="src/main/kotlin/templates"

    mkdir -p "$day_dir"
    mkdir -p "$test_dir"

    # Define file paths
    local input_file="$day_dir/input.txt"
    local solution_file="$day_dir/Solution.kt"
    local test_file="$test_dir/SolutionTest.kt"
    local test_input_file="$test_dir/test_input.txt"
    local solution_template_file="$templates_dir/SolutionTemplate.kt"
    local test_template_file="$templates_dir/TestTemplate.kt"

    if [ -f "$input_file" ]; then
        echo "Cannot create input file since $input_file already exists!"
        exit 1
    fi

    cookie_val=$(get_cookie)
    # Create input file
    new_day_input_file "$input_url" "$cookie_val" "$input_file"

    # Create Kotlin solution file from template
    if [ ! -f "$solution_file" ]; then
        cp $solution_template_file "$solution_file"
        # Update package and file names
        if [[ "$OSTYPE" == "darwin"* ]]; then
            # macOS
            sed -i '' "s/YEAR/$year/g" "$solution_file"
            sed -i '' "s/DAY/$padded_day/g" "$solution_file"
        else
            # Linux/Unix
            sed -i "s/YEAR/$year/g" "$solution_file"
            sed -i "s/DAY/$padded_day/g" "$solution_file"
        fi
        echo "Created Kotlin solution file: $solution_file"
    fi

    # Create test file from template
    if [ ! -f "$test_file" ]; then
        cp $test_template_file "$test_file"
        # Update package and file names
        if [[ "$OSTYPE" == "darwin"* ]]; then
            # macOS
            sed -i '' "s/YEAR/$year/g" "$test_file"
            sed -i '' "s/DAY/$padded_day/g" "$test_file"
        else
            # Linux/Unix
            sed -i "s/YEAR/$year/g" "$test_file"
            sed -i "s/DAY/$padded_day/g" "$test_file"
        fi
        echo "Created test file: $test_file"
    fi

     # Create test input file if it doesn't exist
    if [ ! -f "$test_input_file" ]; then
        # Create an empty test input file that can be filled with example data
        touch "$test_input_file"
        echo "Created test input file: $test_input_file"
        echo "Remember to add example input data to $test_input_file"
    fi
}

function new_day_input_file {
    local input_url=$1
    local cookie_val=$2
    local output_file=$3

    if ! curl "$input_url" --compressed -H "Cookie: session=${cookie_val}" -o "$output_file" --fail; then
        echo "Failed to fetch Advent of Code input! Could not create $output_file"
        rm -f "$output_file"
        exit 1
    else
        echo "Successfully downloaded Advent of Code input to $output_file"
    fi
}

function get_cookie {
    dot_env=".env"
    echo $(grep 'SESSION_COOKIE=' $dot_env | cut -d= -f2)
}

# Entry point
new_day "$selected_year" "$selected_day"
