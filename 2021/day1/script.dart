import 'dart:io';

void main() {
  var inputFile = File('input.txt');
  final lines = inputFile.readAsLinesSync().map(int.parse);
  var lastDepth = lines.first;
  var result = 0;
  for (final depth in lines) {
    if (depth > lastDepth) result++;
    lastDepth = depth;
  }
  print(result);
}
