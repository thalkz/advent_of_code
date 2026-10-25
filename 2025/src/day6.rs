use std::{num::ParseIntError, str::FromStr};

use crate::file;

pub fn solve() -> anyhow::Result<String> {
    let content: String = file::read_input("res/6.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(input: &str) -> anyhow::Result<u64> {
    let sheet: MathSheet = input.parse()?;

    let total: u64 = sheet
        .operations
        .iter()
        .enumerate()
        .map(|(index, &operation)| {
            let values = sheet.values.iter().map(|line| line[index]);
            match operation {
                MathOperation::Add => values.sum::<u64>(),
                MathOperation::Multiply => values.product(),
            }
        })
        .sum();
    Ok(total)
}

struct MathSheet {
    values: Vec<Vec<u64>>,
    operations: Vec<MathOperation>,
}

#[derive(Clone, Copy, Debug)]
enum MathOperation {
    Add,
    Multiply,
}

impl FromStr for MathOperation {
    type Err = anyhow::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "+" => Ok(MathOperation::Add),
            "*" => Ok(MathOperation::Multiply),
            _ => anyhow::bail!("failed to parse {} into an operation", s),
        }
    }
}

impl FromStr for MathSheet {
    type Err = anyhow::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let values: Vec<Vec<u64>> = s
            .lines()
            .map(str::trim)
            .filter(|l| !l.is_empty())
            .filter_map(|l| {
                l.split_ascii_whitespace()
                    .map(|v| v.parse::<u64>())
                    .collect::<Result<Vec<u64>, ParseIntError>>()
                    .ok()
            })
            .collect();

        let operations_line: &str = s
            .lines()
            .map(str::trim)
            .filter(|l| !l.is_empty())
            .skip(values.len())
            .next()
            .ok_or(anyhow::format_err!("failed to get op line"))?;

        let operations = parse_operation_line(operations_line)?;

        Ok(MathSheet { values, operations })
    }
}

fn parse_operation_line(line: &str) -> anyhow::Result<Vec<MathOperation>> {
    line.split_ascii_whitespace()
        .map(|v| v.parse::<MathOperation>())
        .collect::<Result<Vec<MathOperation>, anyhow::Error>>()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_day6() {
        let input = r"123 328  51 64
         45 64  387 23
          6 98  215 314
        *   +   *   +";
        let actual = solve_for(input).unwrap();
        assert_eq!(actual, 3263827);
    }
}
