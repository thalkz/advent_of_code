use std::{ops::RangeInclusive, str::FromStr};

use crate::file;

pub fn solve() -> anyhow::Result<String> {
    let content: String = file::read_input("res/5.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(input: &str) -> anyhow::Result<u64> {
    let db: Database = input.parse()?;

    let result: usize = db
        .ids
        .iter()
        .filter(|id| db.fresh.iter().any(|range| range.contains(id)))
        .count();

    Ok(result as u64)
}

struct Database {
    fresh: Vec<RangeInclusive<u64>>,
    ids: Vec<u64>,
}

impl FromStr for Database {
    type Err = anyhow::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut after = false;
        let mut fresh = Vec::new();
        let mut ids = Vec::new();
        for line in s.lines().map(str::trim) {
            if line.is_empty() {
                after = true;
                continue;
            }
            if !after {
                let (start, end) = line
                    .split_once("-")
                    .ok_or(anyhow::Error::msg("should contain '-' character"))?;
                let start = start.parse::<u64>()?;
                let end = end.parse::<u64>()?;
                fresh.push(start..=end);
            } else {
                ids.push(line.parse::<u64>()?);
            }
        }
        Ok(Database { fresh, ids })
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_day5() {
        let input = r"3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32";

        let actual = solve_for(input).unwrap();
        assert_eq!(actual, 3);
    }
}
