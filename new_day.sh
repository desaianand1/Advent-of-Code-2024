#!/bin/bash

# Parse command-line arguments
# Check if two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <yyyy> <d>"
    exit 1
fi

# Parse and validate the arguments
selected_year=$1
if ! [[ $selected_year =~ ^[0-9]{4}$ ]]; then
    echo "Error: Invalid year format. Please provide a 4-digit year (yyyy)."
    exit 1
fi

# Parse and validate the second argument (day)
selected_day=$2
if ! [[ $selected_day =~ ^[1-9]$|^1[0-9]$|^2[0-5]$ ]]; then
    echo "Error: Invalid day format. Please provide a valid day (1-25)."
    exit 1
fi

# Pad day with leading zero if necessary
padded_day=$(printf "%02d" $selected_day)

function new_day {
    local year=$1
    local day=$2
    local padded_day=$(printf "%02d" $day)
    local domain="adventofcode.com"
    local input_url="https://$domain/$year/day/$day/input"
    
    # Create directory structure
    local day_dir="$year/day$padded_day"
    mkdir -p $day_dir

    # Define file paths
    local input_file="$day_dir/input.txt"
    local solution_file="$day_dir/solution.kt"
    local test_file="$day_dir/test.kt"

    if [ -f $input_file ]; then
        echo "Cannot create input file since $input_file already exists!"
        exit 1
    fi

    cookie_val=$(get_cookie)
    # Create input file
    new_day_input_file $input_url $cookie_val $input_file

    # Create Kotlin solution file from template
    if [ ! -f $solution_file ]; then
        cp utilities/template.kt $solution_file
        # Update package and file names
        sed -i "s/YEAR/$year/g" $solution_file
        sed -i "s/DAY/$padded_day/g" $solution_file
        echo "Created Kotlin solution file: $solution_file"
    fi

    # Create test file from template
    if [ ! -f $test_file ]; then
        cp utilities/template_test.kt $test_file
        # Update package and file names
        sed -i "s/YEAR/$year/g" $test_file
        sed -i "s/DAY/$padded_day/g" $test_file
        echo "Created test file: $test_file"
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
new_day $selected_year $selected_day