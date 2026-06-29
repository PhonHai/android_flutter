import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_module/app.dart';

void main() {
  testWidgets('Pomodoro app renders timer', (WidgetTester tester) async {
    await tester.pumpWidget(const ProviderScope(child: PomodoroApp()));
    await tester.pump();

    // 验证计时器显示 25:00
    expect(find.text('25:00'), findsOneWidget);

    // 验证时长选择按钮存在
    expect(find.text('25 分钟'), findsOneWidget);
    expect(find.text('15 分钟'), findsOneWidget);
    expect(find.text('45 分钟'), findsOneWidget);
  });
}
