extension [[Character]] {
    func at(_ pos: Point) -> Character {
        if pos.x < 0 || pos.x >= self[0].count || pos.y < 0 || pos.y >= self.count {
            return "."
        }
        return self[pos.y][pos.x]
    }
    
    func adjacent(_ p: Point) -> [Point] {
        var result: [Point] = []
        if p.x >= 0 {
            result.append(Point(x: p.x-1, y: p.y))
        }
        if p.x <= self[0].count - 1 {
            result.append(Point(x: p.x+1, y: p.y))
        }
        if p.y >= 0 {
            result.append(Point(x: p.x, y: p.y-1))
        }
        if p.y <= self.count - 1  {
            result.append(Point(x: p.x, y: p.y+1))
        }
        return result
    }
    
    func adjacentPipe(p: Point) -> [Point] {
        var result: [Point] = []
        let char = self.at(p)
        if p.x > 0 && char.left {
            let adj = Point(x: p.x-1, y: p.y)
            if self.at(adj).right {
                result.append(adj)
            }
        }
        if p.x < self[0].count - 1 && char.right {
            let adj = Point(x: p.x+1, y: p.y)
            if self.at(adj).left {
                result.append(adj)
            }
        }
        if p.y > 0 && char.up {
            let adj = Point(x: p.x, y: p.y-1)
            if self.at(adj).down {
                result.append(adj)
            }
        }
        if p.y < self.count - 1 && char.down {
            let adj = Point(x: p.x, y: p.y+1)
            if self.at(adj).up {
                result.append(adj)
            }
        }
        return result
    }
}
