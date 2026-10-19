extension Character {
    var right: Bool {
        get {
            return self == "-" || self == "L" || self == "F" || self == "S"
        }
    }
    
    var left: Bool {
        get {
            return self == "-" || self == "J" || self == "7" || self == "S"
        }
    }
    
    var up: Bool {
        get {
            return self == "|" || self == "L" || self == "J" || self == "S"
        }
    }
    
    var down: Bool {
        get {
            return self == "|" || self == "F" || self == "7" || self == "S"
        }
    }
}
