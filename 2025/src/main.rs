use std::{fmt::Display, time::Instant};

mod day1;
mod day2;
mod day3;
mod file;

fn main() {
    println!("Advent of Code 2025, in Rust!");
    print_day(1, day1::solve, "5923");
    print_day(2, day2::solve, "25663320831");
    print_day(3, day3::solve, "173577199527257");
}

fn print_day<E: Display>(day: u8, solver: fn() -> Result<String, E>, expected: &str) {
    let start = Instant::now();
    let result = solver();
    let ms = start.elapsed().as_millis();
    match result {
        Ok(result) if result == expected => {
            println!("day {}: {} (in {}ms) - CORRECT", day, result, ms)
        }
        Ok(result) => println!("day {}: {} (in {}ms) - WRONG", day, result, ms),
        Err(err) => println!("day {}: {}", day, err),
    }
}
