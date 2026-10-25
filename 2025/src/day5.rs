use std::{ops::RangeInclusive, str::FromStr};

use crate::file;

pub fn solve() -> anyhow::Result<String> {
    let content: String = file::read_input("res/5.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(input: &str) -> anyhow::Result<u64> {
    let mut db: Database = input.parse()?;

    db.fresh.sort_by_key(|range| *range.start());

    let mut cursor: u64 = 0;
    let mut result: u64 = 0;
    for range in &db.fresh {
        let &to = range.end();
        if cursor > to {
            continue;
        }
        let from = (*range.start()).max(cursor);
        result += (to - from) + 1;
        cursor = to + 1;
    }

    Ok(result as u64)
}

struct Database {
    fresh: Vec<RangeInclusive<u64>>,
}

impl FromStr for Database {
    type Err = anyhow::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut fresh = Vec::new();
        for line in s.lines().map(str::trim) {
            if line.is_empty() {
                break;
            }
            let (start, end) = line
                .split_once("-")
                .ok_or(anyhow::Error::msg("should contain '-' character"))?;
            let start = start.parse::<u64>()?;
            let end = end.parse::<u64>()?;
            fresh.push(start..=end);
        }
        Ok(Database { fresh })
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
        assert_eq!(actual, 14);
    }
}
