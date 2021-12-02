import 'dart:io';

void main() {
  var inputFile = File('input.txt');
  final lines = inputFile.readAsLinesSync().map(int.parse).toList();
  final averages = [];

  for (int i = 0; i < lines.length - 2; i++) {
    averages.add(lines[i] + lines[i + 1] + lines[i + 2]);
  }

  var lastDepth = averages.first;
  var result = 0;
  for (int depth in averages) {
    if (depth > lastDepth) result++;
    lastDepth = depth;
  }
  print(result);
}
