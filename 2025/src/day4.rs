use std::str::FromStr;

use crate::file;

pub fn solve() -> anyhow::Result<String> {
    let content: String = file::read_input("res/4.txt")?;
    solve_for(&content).map(|x| x.to_string())
}

fn solve_for(input: &str) -> anyhow::Result<u64> {
    let mut grid: Grid = input.parse()?;

    let mut result = 0;
    let mut to_remove: Vec<(usize, usize)> = Vec::new();

    loop {
        (0..grid.height)
            .flat_map(|y| (0..grid.width).map(move |x| (x, y)))
            .filter(|&(x, y)| grid.get(x, y) == Some(Cell::Roll))
            .filter(|&(x, y)| grid.surrounding_cells(x, y) < 4)
            .for_each(|(x, y)| {
                result += 1;
                to_remove.push((x, y));
            });

        if to_remove.is_empty() {
            return Ok(result);
        }

        for &(x, y) in &to_remove {
            grid.remove_at(x, y);
        }

        to_remove.clear();
    }
}

struct Grid {
    width: usize,
    height: usize,
    cells: Vec<Cell>,
}

impl Grid {
    fn index(&self, x: usize, y: usize) -> Option<usize> {
        if x >= self.width || y >= self.height {
            None
        } else {
            Some(y * self.width + x)
        }
    }

    fn get(&self, x: usize, y: usize) -> Option<Cell> {
        self.index(x, y)
            .and_then(|idx| self.cells.get(idx).copied())
    }

    fn remove_at(&mut self, x: usize, y: usize) {
        if let Some(i) = self.index(x, y) {
            self.cells[i] = Cell::Empty;
        }
    }

    fn surrounding_cells(&self, x: usize, y: usize) -> usize {
        const OFFSETS: [(isize, isize); 8] = [
            (-1, -1),
            (-1, 0),
            (-1, 1),
            (0, -1),
            (0, 1),
            (1, -1),
            (1, 0),
            (1, 1),
        ];

        OFFSETS
            .iter()
            .filter_map(|offset| {
                let x = x.checked_add_signed(offset.0)?;
                let y = y.checked_add_signed(offset.1)?;
                self.get(x, y)
            })
            .filter(|&cell| cell == Cell::Roll)
            .count()
    }
}

#[derive(Debug, Clone, Copy, PartialEq)]
enum Cell {
    Empty,
    Roll,
}

impl TryFrom<char> for Cell {
    type Error = anyhow::Error;

    fn try_from(value: char) -> Result<Self, Self::Error> {
        match value {
            '@' => Ok(Cell::Roll),
            '.' => Ok(Cell::Empty),
            _ => anyhow::bail!("should be @ or ."),
        }
    }
}

impl FromStr for Grid {
    type Err = anyhow::Error;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut width = 0;
        let mut height = 0;
        let mut cells = Vec::new();
        for line in s.lines().map(str::trim).filter(|l| !l.is_empty()) {
            height += 1;
            width = line.chars().count();
            for c in line.chars() {
                cells.push(Cell::try_from(c)?);
            }
        }
        Ok(Grid {
            width,
            height,
            cells,
        })
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_day4() {
        let input = r"..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.";

        let actual = solve_for(input).unwrap();
        assert_eq!(actual, 43);
    }
}
