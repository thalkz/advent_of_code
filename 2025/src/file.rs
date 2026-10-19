use std::{fs, io};

pub fn read_input(filepath: &str) -> Result<String, io::Error> {
    let content = fs::read_to_string(filepath)?;
    Ok(content)
}
