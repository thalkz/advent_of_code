use std::{collections::HashSet, ops};

use crate::file;

type Result<T> = core::result::Result<T, Box<dyn std::error::Error>>;

pub fn solve() -> Result<String> {
    let content: String = file::read_input("res/2.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(input: &str) -> Result<u64> {
    let ranges: Vec<ops::RangeInclusive<u64>> = parse(input)?;

    let max_value = ranges
        .iter()
        .map(|r| *r.end())
        .max()
        .expect("ranges should have a max");
    let invalid_ids: HashSet<u64> = invalid_ids(max_value)
        .filter(|invalid_id| ranges.iter().any(|r| r.contains(invalid_id)))
        .collect();

    let sum: u64 = invalid_ids.iter().sum();

    Ok(sum)
}

fn count_digits(x: u64) -> u32 {
    x.ilog10() + 1
}

fn invalid_ids(max_value: u64) -> impl Iterator<Item = u64> {
    let max_digits = count_digits(max_value);
    (1..=max_digits).flat_map(move |digits| {
        (1..digits)
            .filter(move |part_size| digits % part_size == 0)
            .flat_map(move |part_size| {
                let parts = digits / part_size;
                invalid_ids_for(parts, part_size, max_value)
            })
    })
}

fn invalid_ids_for(parts: u32, part_size: u32, max_value: u64) -> impl Iterator<Item = u64> {
    let max: u64 = 10_u64.pow(part_size);
    let min: u64 = 10_u64.pow(part_size - 1);
    (min..max)
        .map(move |part_value| generate_from_parts(part_value, parts, part_size))
        .take_while(move |&x| x <= max_value)
}

fn generate_from_parts(part_value: u64, parts: u32, part_size: u32) -> u64 {
    let factor = (10_u64.pow(parts * part_size) - 1) / (10_u64.pow(part_size) - 1);
    part_value * factor
}

fn parse(input: &str) -> Result<Vec<ops::RangeInclusive<u64>>> {
    input
        .trim()
        .split(',')
        .map(|range| {
            let (start, end) = range.split_once('-').ok_or("expected - delimiter")?;
            let start = start.parse()?;
            let end: u64 = end.parse()?;
            Ok(start..=end)
        })
        .collect()
}

#[cfg(test)]
mod tests {
    use crate::day2::*;

    #[test]
    fn test_generate_from_parts() {
        let result = generate_from_parts(21, 5, 2);
        assert_eq!(result, 2121212121);
    }

    #[test]
    fn test_count_digits() {
        let result = count_digits(21731);
        assert_eq!(result, 5);
    }

    #[test]
    fn test_day2_small() {
        let test_input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";
        let result = solve_for(test_input).unwrap();
        assert_eq!(result, 4174379265);
    }
}
