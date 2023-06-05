# Scala-Pattern-Matching
This Scala program performs pattern matching on strings using only the concatenation, alternation ("|"), and optional ("?") operators of regular expressions. It represents expressions as trees using case classes and evaluates the inputs without using any regular expression libraries.

## Requirements
To run the program, you need to have Scala installed on your machine. If Scala is not installed, you can download and install it from the official Scala website: scala-lang.org

## Usage
1. Clone the repository or download the source code files.
2. Open a terminal or command prompt and navigate to the project directory.
3. Compile the Scala source files using the following command:
```
scalac Main.scala
```
4. Run the program using the following command:
```
scala Main
```
5. Follow the on-screen prompts to input a pattern and strings to match against the pattern. The program will report whether each string matches the pattern.

Examples
Here are some example patterns and their matching results:

Pattern: ((h|j)ell. worl?d)|(42)

- Input: hello world
    - Match
- Input: jello word
    - Match
- Input: jelly word
    - Match
- Input: 42
    - Match
- Input: 24
    - No match
- Input: hello world42
    - No match

Pattern: I (like|love|hate)( (cat|dog))? people

- Input: I like cat people
    - Match
- Input: I love dog people
    - Match
- Input: I hate people
    - Match
- Input: I likelovehate people
    - No match
- Input: I people
    - No match

## Grammar
The program uses the following grammar to parse the patterns:
```
S  -: E$

E  -: T E2

E2 -: '|' E3

E2 -: NIL

E3 -: T E2

T  -: F T2

T2 -: F T2

T2 -: NIL

F  -: A F2

F2 -: '?' F2

F2 -: NIL

A  -: C

A  -: '(' A2

A2 -: E ')'

```

An ambiguous grammar for patterns is:
```
S -: E$ 

E -: C | EE | E'|'E | E'?' | '(' E ')'

C -: '0' | '1' | ... | '9'| 'a' | 'b' | ... | 'z' | '.'
```

To reflect that option(‘?’) has highest precedence, then concatenation, then alternation(‘|’), the following unambiguous grammar created:
```

 S  -: E$
 
 E  -: T '|' E | T
 
 T  -: F T | F
 
 F  -: A '?' | A
 
 A  -: C | '(' E ')'
 ```
