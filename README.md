## Overview
This project is a re-implementation of the WiredRobot Abstract Data Type (ADT) using a Binary Search Tree (BST) for efficient insertion and lookup of functional parts. The project is part of the CS 351/CST 751: Data Structures & Algorithms coursework, focusing on enhancing understanding and application of recursive data structures.

## Features
- **Binary Search Tree (BST) Implementation**: The WiredRobot ADT is implemented using an endogenous BST without balancing, allowing for efficient operations.
- **Handling Duplicates**: Parts with equivalent comparators are ordered by unique IDs to manage duplicates.
- **Recursive Helper Methods**: Key operations such as checking the range, adding parts, and comparing elements are handled through recursive methods.
- **Efficiency**: The implementation is optimized to handle a large number of entries, ensuring logarithmic time complexity where applicable.

## Learning Objectives
- Implement a sorted endogenous container ADT with a BST.
- Add data to a BST efficiently.
- Ensure the structural integrity of a BST.
- Utilize recursion effectively in helper methods.
- Analyze and optimize structurally recursive code.

## Project Structure
- **WiredRobot.java**: The main class implementing the WiredRobot ADT using a BST.
- **UnlockTest.java**: Unlocks the provided tests without running them.
- **TestInternals.java**: Tests helper methods and the structural integrity of the BST.
- **TestWiredRobot.java**: JUnit tests for the ADT, excluding removal and getNext methods.
- **TestExhaustive.java**: Exhaustive JUnit test suite for all possible small BSTs.
- **TestEfficiency.java**: Tests ensuring the efficiency of the BST implementation.

## Getting Started
1. Clone the repository.
2. Ensure you have a Java development environment set up.
3. Review the project files and understand the structure.
4. Implement the required methods, focusing on recursion and BST integrity.
5. Run the provided test cases to validate your implementation.

## How to Run Tests
- Use the provided JUnit test files to check the correctness, efficiency, and integrity of your implementation.
- The `TestEfficiency.java` file is particularly important for ensuring your implementation scales well with large data sets.
