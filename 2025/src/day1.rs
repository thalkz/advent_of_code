use crate::file;

type Result<T> = core::result::Result<T, Box<dyn std::error::Error>>;

pub fn solve() -> Result<String> {
    let content: String = file::read_input("res/1.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(content: &str) -> Result<i32> {
    let mut zeros = 0;
    let mut cursor: i32 = 50;
    for line in content.lines().map(str::trim).filter(|l| !l.is_empty()) {
        let distance: i32 = line[1..].parse().unwrap();
        match line.chars().next() {
            Some('R') => {
                zeros += (cursor + distance).div_euclid(100);
                cursor = (cursor + distance).rem_euclid(100);
            }
            Some('L') => {
                let x = cursor - distance;
                if x <= 0 {
                    zeros += (-x).div_euclid(100);
                    if cursor > 0 {
                        zeros += 1
                    }
                }
                cursor = (cursor - distance).rem_euclid(100)
            }
            _ => panic!("should be R or L"),
        }
        //println!("after line {line}: {zeros} zeros");
    }
    Ok(zeros)
}

#[cfg(test)]
mod tests {
    use crate::day1::*;

    const TEST_INPUT: &str = "L68
L30
R48
L5
R60
L55
L1
L99
R14
L82";

    #[test]
    fn test_small() {
        let result = solve_for(TEST_INPUT).unwrap();
        assert_eq!(result, 6);
    }
}
