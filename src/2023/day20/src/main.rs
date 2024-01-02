use std::collections::HashMap;

fn main() {
    println!("Part 1: {}", part1(include_str!("./input.txt")));
    println!("Part 2: {}", part2(include_str!("./input.txt")));
}

trait Module {
    fn recv(&mut self, name: String, high: bool);
    fn send(&mut self) -> Vec<(String, String, bool)>;
}

struct Broadcaster {
    name: String,
    dest: Vec<String>,
}

impl Module for Broadcaster {
    fn recv(&mut self, _name: String, _high: bool) {}
    fn send(&mut self) -> Vec<(String, String, bool)> {
        let mut res = Vec::new();
        for d in self.dest.iter() {
            res.push((d.clone(), self.name.clone(), false));
        }
        res
    }
}

struct FlipFlop {
    name: String,
    on: bool,
    last: bool,
    dest: Vec<String>,
}

impl Module for FlipFlop {
    fn recv(&mut self, _name: String, high: bool) {
        self.last = high;
    }
    
    fn send(&mut self) -> Vec<(String, String, bool)> {
        if self.last {
            return vec![];
        }
        self.on = !self.on;
        let mut res = Vec::new();
        for d in self.dest.iter() {
            res.push((d.clone(), self.name.clone(), self.on));
        }
        res
    }
}

struct Conjunction {
    // previous name -> last signal sent
    name: String,
    states: HashMap<String, bool>,
    dest: Vec<String>,
}

impl Module for Conjunction {
    fn recv(&mut self, name: String, high: bool) {
        self.states.insert(name, high);
    }
    
    fn send(&mut self) -> Vec<(String, String, bool)> {
        let high = !self.states.values().all(|&value| value);
        let mut res = Vec::new();
        for d in self.dest.iter() {
            res.push((d.clone(), self.name.clone(), high));
        }
        res       
    }
}

fn part1(input: &str) -> u64 {
    let mut hm: HashMap<String, Box<dyn Module>> = HashMap::new();
    let mut inputs = HashMap::new();
    let mut conjunctions = Vec::new();
    for line in input.lines() {
        let parts = line.split(" -> ").collect::<Vec<_>>();
        let dest = parts[1].split(", ").map(|s| s.to_string()).collect::<Vec<_>>();
        let mut name: &str = "";
        if parts[0] == "broadcaster" {
            name = parts[0];
            hm.insert(parts[0].to_string(), Box::new(Broadcaster { name: name.to_string(), dest: dest.clone() }));
        } else if parts[0].chars().nth(0).unwrap() == '%' {
            name = &parts[0][1..];
            hm.insert(name.to_string(), Box::new(FlipFlop { name: name.to_string(), on: false, last: false, dest: dest.clone() }));
        } else {
            name = &parts[0][1..];
            conjunctions.push(name);
            hm.insert(name.to_string(), Box::new(Conjunction { name: name.to_string(), states: HashMap::new(), dest: dest.clone() }));
        }
        for d in dest {
            inputs.entry(d).or_insert(vec![]).push(name.to_string());
        }
    }
    hm.insert("button".to_string(), Box::new(Broadcaster { name: "button".to_string(), dest: vec!["broadcaster".to_string()] }));
    for name in conjunctions.into_iter() {
        let conj = hm.get_mut(&name.to_string()).unwrap();
        for input in inputs.get(&name.to_string()).unwrap() {
            conj.recv(input.clone(), false);
        }
    }
    let mut lows = 0;
    let mut highs = 0;
    for _step in 0..1000 {
        let mut queue = Vec::new();
        queue.push(("broadcaster".to_string(), "button".to_string(), false));
        while queue.len() > 0 {
            let mut next_queue = Vec::new();
            for (module_name, prev_name, high) in queue.iter() {
                if *high {
                    highs += 1;
                } else {
                    lows += 1;
                }
                if hm.contains_key(module_name) {
                    let module = hm.get_mut(module_name).unwrap();
                    module.recv(prev_name.to_string(), *high);
                    next_queue.extend(module.send());
                }
            }
            queue = next_queue;
        }
    }
    lows * highs
}

fn part2(input: &str) -> u64 {
    let mut hm: HashMap<String, Box<dyn Module>> = HashMap::new();
    let mut inputs = HashMap::new();
    let mut conjunctions = Vec::new();
    for line in input.lines() {
        let parts = line.split(" -> ").collect::<Vec<_>>();
        let dest = parts[1].split(", ").map(|s| s.to_string()).collect::<Vec<_>>();
        let mut name: &str = "";
        if parts[0] == "broadcaster" {
            name = parts[0];
            hm.insert(parts[0].to_string(), Box::new(Broadcaster { name: name.to_string(), dest: dest.clone() }));
        } else if parts[0].chars().nth(0).unwrap() == '%' {
            name = &parts[0][1..];
            hm.insert(name.to_string(), Box::new(FlipFlop { name: name.to_string(), on: false, last: false, dest: dest.clone() }));
        } else {
            name = &parts[0][1..];
            conjunctions.push(name);
            hm.insert(name.to_string(), Box::new(Conjunction { name: name.to_string(), states: HashMap::new(), dest: dest.clone() }));
        }
        for d in dest {
            inputs.entry(d).or_insert(vec![]).push(name.to_string());
        }
    }
    hm.insert("button".to_string(), Box::new(Broadcaster { name: "button".to_string(), dest: vec!["broadcaster".to_string()] }));
    for name in conjunctions.into_iter() {
        let conj = hm.get_mut(&name.to_string()).unwrap();
        for input in inputs.get(&name.to_string()).unwrap() {
            conj.recv(input.clone(), false);
        }
    }
    let mut sources = HashMap::new();
    for module_name in inputs.get(&"xn".to_string()).unwrap() {
        sources.insert(module_name.to_string(), 0);
    }
    let mut step = 1;
    println!("{:?}", sources);
    while !sources.values().all(|&x| x > 0) {
        let mut queue = Vec::new();
        queue.push(("broadcaster".to_string(), "button".to_string(), false));
        while queue.len() > 0 {
            let mut next_queue = Vec::new();
            for (module_name, prev_name, high) in queue.iter() {
                if module_name == "xn" && *high {
                    let val = sources.get(prev_name).unwrap();
                    if *val == 0 {
                        sources.insert(prev_name.to_string(), step);
                    }
                }
                if hm.contains_key(module_name) {
                    let module = hm.get_mut(module_name).unwrap();
                    module.recv(prev_name.to_string(), *high);
                    next_queue.extend(module.send());
                }
            }
            queue = next_queue;
        }
        step += 1;
    }
    sources.values().product()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_example() {
        assert_eq!(part1(include_str!("./example.txt")), 11687500);
        assert_eq!(part1(include_str!("./example2.txt")), 32000000);
    }
}
