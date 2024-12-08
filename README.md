# 🎄 Advent of Code 2024 📅

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

My attempt at Advent of Code 2024, brushing up on Kotlin while at it

**Disclaimer:** `input.txt`(s) have been omitted as I was made aware
that [they are not to be publicly shared](https://adventofcode.com/about#faq_copying).

## Table of Contents

- [Overview](#overview)
- [Setup](#setup)
    - [Prerequisites](#prereq)
    - [Session Cookies](#cookie)
        - [Firefox](#firefox)
        - [Chrome](#chrome)
    - [Generating a New Day](#new-day)
- [Running Code](#run-code)
    - [Testing](#testing)

## Overview <a name="overview"></a>

| Day | Name                  | Stars |
|-----|-----------------------|-------|
| 01  | Historian Hysteria    | ⭐ ⭐   |
| 02  | Red-Nosed Reports     | ⭐ ⭐   |
| 03  | Mull It Over          | ⭐ ⭐   |
| 04  | Ceres Search          | ⭐ ⭐   |
| 05  | Print Queue           | ⭐ ⭐   |
| 06  | Guard Gallivant       | ⭐ ⭐   |
| 07  | Bridge Repair         | ⭐ ⭐   |
| 08  | Resonant Collinearity | ⭐ ⭐   |
| 09  |                       |       |
| 10  |                       |       |
| 11  |                       |       |
| 12  |                       |       |
| 13  |                       |       |
| 14  |                       |       |
| 15  |                       |       |
| 16  |                       |       |
| 17  |                       |       |
| 18  |                       |       |
| 19  |                       |       |
| 20  |                       |       |
| 21  |                       |       |
| 22  |                       |       |
| 23  |                       |       |
| 24  |                       |       |
| 25  |                       |       |

## Setup <a name="setup"></a>

### Prerequisites <a name="prereq"></a>

I solved all of this year's problems in Kotlin. To get started, you'll need to follow the respective installation
instructions:

- [JDK 21 or later (use your favorite package manager to do so)](https://openjdk.org/install/)
- [Kotlin 2.1 or later](https://kotlinlang.org/docs/getting-started.html)
- [Gradle 8.11 or later](https://gradle.org/install/)

### Git Hooks

This project uses Git hooks to ensure code quality. To install the hooks:

```bash
# Make the install script executable
chmod +x setup/install-hooks.sh

# Install the hooks
./setup/install-hooks.sh
```

The pre-commit hook will:

1. Format all Kotlin files using ktlint
2. Run all tests
3. Prevent commit if either step fails
4. Show modified files if formatting changed anything

### 🍪 Session Cookies <a name="cookie"></a>

The `new_day.sh` script uses Advent of Code's
authentication [session cookie](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies) to auto-fetch the day's input
for the current year.

To get your own cookie, visit [Advent of Code](https://adventofcode.com/). Once logged in:

#### Firefox <a name="firefox"></a>

- Right-click and select "Inspect". In the "Storage" tab, expand "Cookies" and select `https://adventofcode.com`. Copy
  the cookie titled "session"

#### Chrome <a name="chrome"></a>

- Right-click and select "Inspect Element". In the "Application" tab, under "Storage", expand "Cookies" and select
  `https://adventofcode.com`. Copy the cookie titled "session"

Paste your session cookie data into a newly created `.env` file. (`.env.example` provides a structural example)

### 📆 Generating a New Day <a name="new-day"></a>

- Add your [Advent of Code session cookie](#cookie) to the `.env` file.

- Option 1: Run `new_day.sh` to create the current day's directory
- Option 2: Run `new_day.sh d` where `d` is a day between `1` - `25` to create that day's directory for the current
  year (if it doesn't already exist)
- Option 3: Run `new_day.sh yyyy d` where `yyyy` is a 4-digit year between `2015` and the current year, `d` is a day
  between `1` - `25` to create that date's directory (if it doesn't already exist)
- Navigate to the generated day's directory, equipped with the day's input and some boilerplate Kotlin files

> [!IMPORTANT]
> This script was only intended to be run during the duration of Advent of Code (i.e. Dec 1 through 25 of a given year).

## Running Code <a name="run-code"></a>

To run the code for day `d`, execute the following, replacing `<d>` with the specific day (e.g. 1 - 25)

```sh
# Run a specific day
./gradlew day -Pyear=2024 -Pday=1

# For single-digit days, both formats work:
./gradlew day -Pyear=2024 -Pday=1
./gradlew day -Pyear=2024 -Pday=01
```

### Testing <a name="testing"></a>

Solutions are verified against known answers using JUnit tests. To run tests:

```sh
./gradlew test              # Run all tests
./gradlew test --tests "2024.day01.*"  # Run specific day's tests
```
