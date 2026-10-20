use std::ops::Range;

use crate::file;

pub fn solve() -> anyhow::Result<String> {
    let content: String = file::read_input("res/3.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(input: &str) -> anyhow::Result<u64> {
    let result: u64 = input.lines().map(compute_joltage).sum();
    Ok(result)
}

const DIGITS_LEN: u32 = 12;

fn compute_joltage(line: &str) -> u64 {
    let digits: Vec<u32> = line
        .trim()
        .chars()
        .map(|char| char.to_digit(10).unwrap())
        .collect();

    let mut start: usize = 0;
    let end: usize = digits.len() - 11;
    let mut result: u64 = 0;

    for i in 0..12 {
        let (index, digit) = find_next_digit(&digits, start..(end + i as usize));
        result += (digit as u64) * 10_u64.pow(DIGITS_LEN - i - 1);
        start += index + 1;
    }

    result
}

fn find_next_digit(digits: &[u32], range: Range<usize>) -> (usize, u32) {
    let (index, &digit) = digits[range]
        .iter()
        .enumerate()
        .rev()
        .max_by_key(|(_, char)| *char)
        .unwrap();
    (index, digit)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_small_input() {
        let input = r"987654321111111
            811111111111119
            234234234234278
            818181911112111";

        let actual = solve_for(input).unwrap();
        assert_eq!(actual, 3121910778619);
    }
}
