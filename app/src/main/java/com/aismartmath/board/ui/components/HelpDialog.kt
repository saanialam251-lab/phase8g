package com.aismartmath.board.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.Canvas
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val Gold = Color(0xFFFBBF24)
private val Pink = Color(0xFFF472B6)
private val Green = Color(0xFF34D399)
private val Cream = Color(0xFFF1D3A5)
private val Copper = Color(0xFFD08A4E)
private val GridC = Color(0x44F1D3A5)

private class HelpTopic(val id: String, val title: String, val lines: List<String>, val pic: @Composable () -> Unit)

private fun DrawScope.axes() {
    val w = size.width
    val h = size.height
    drawLine(GridC, Offset(0f, h * 0.7f), Offset(w, h * 0.7f), 2f)
    drawLine(GridC, Offset(w / 2, 0f), Offset(w / 2, h), 2f)
}

private fun DrawScope.plotFn(col: Color, x0: Float, x1: Float, width: Float = 5f, f: (Float) -> Float) {
    val w = size.width
    val h = size.height
    val p = Path()
    var first = true
    var x = x0
    while (x <= x1) {
        val px = w / 2 + x * w / 7f
        val py = h * 0.7f - f(x) * h / 5f
        if (py > -h && py < 2 * h) {
            if (first) {
                p.moveTo(px, py)
                first = false
            } else p.lineTo(px, py)
        }
        x += 0.05f
    }
    drawPath(p, col, style = Stroke(width, cap = StrokeCap.Round))
}

@Composable
private fun Pic(draw: DrawScope.(TextMeasurer) -> Unit) {
    val m = rememberTextMeasurer()
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF160E0A))
            .border(BorderStroke(1.dp, Color(0xFF6B4630)), RoundedCornerShape(12.dp)),
    ) { draw(m) }
}

private fun DrawScope.label(m: TextMeasurer, s: String, x: Float, y: Float, c: Color = Cream, size: Float = 13f) {
    drawText(m, s, Offset(x, y), TextStyle(color = c, fontSize = size.sp, fontWeight = FontWeight.Bold))
}

private val TOPICS: List<HelpTopic> = listOf(
    HelpTopic(
        "graph", "ƒ  Graphs",
        listOf(
            "Open the ƒ Graph tool (toolbar → Graph) and type an equation, then press Plot.",
            "Try:  x^2 - 3x + 1   ·   sin(x)   ·   1/x   ·   sqrt(x)   ·   abs(x).",
            "a, b and c are sliders: a*x^2 + b*x + c lets you change the numbers and watch the curve move. ▶ a animates it.",
            "Tap the curve to see a point on it and its slope. The info bar lists intercepts and turning points.",
            "Use ^ for powers, * for times, / for divide, and | | or abs( ) for absolute value.",
        ),
    ) {
        Pic { m ->
            axes()
            plotFn(Gold, -2.6f, 2.6f) { it * it * 0.5f - 0.5f }
            val tx = 1.2f
            val ty = tx * tx * 0.5f - 0.5f
            val cx = size.width / 2 + tx * size.width / 7f
            val cy = size.height * 0.7f - ty * size.height / 5f
            drawLine(Color(0xFFF87171), Offset(cx - 60f, cy + 60f * 1.2f * 0.7f), Offset(cx + 60f, cy - 60f * 1.2f * 0.7f), 4f, StrokeCap.Round)
            drawCircle(Color.White, 8f, Offset(cx, cy))
            label(m, "y = x²/2 − ½", 12f, 8f)
        }
    },
    HelpTopic(
        "derivative", "dy/dx  Derivatives (slope curves)",
        listOf(
            "Type  dy/dx of x^3 - 3x  and press Plot. You get two curves: your function, and in pink its slope curve dy/dx.",
            "Type  d2y/dx2 of x^3 - 3x  for the second derivative (green).",
            "Other ways that work:  d/dx(sin(x))  ·  derivative of x^2  ·  diff(x^2).",
            "Already plotted a graph? Press the  dy/dx  or  d²y/dx²  button in the graph panel to add its slope curve.",
            "Where the pink curve touches zero, the original curve has a top or a bottom (a turning point).",
            "If you type  dy/dx = 2x  the app just draws 2x, because that is the slope you wrote.",
        ),
    ) {
        Pic { m ->
            axes()
            plotFn(Gold, -2.7f, 2.7f) { it * it * it / 4f - 0.75f * it }
            plotFn(Pink, -2.7f, 2.7f) { 0.75f * it * it - 0.75f }
            label(m, "yellow: y", 12f, 8f, Gold)
            label(m, "pink: dy/dx", 12f, 28f, Pink)
        }
    },
    HelpTopic(
        "shapes", "○ ▭ △  Circles, squares and more by equation",
        listOf(
            "Any equation with x and y draws its curve. Circle:  x^2 + y^2 = 9  (radius 3).   Ellipse:  x^2/9 + y^2/4 = 1.",
            "Square:  max(|x|, |y|) = 3.   Rectangle:  max(|x|/4, |y|/2) = 1.   Rhombus:  |x|/4 + |y|/2 = 1.",
            "Vertical line:  x = 3.   Hyperbola:  x^2/4 - y^2/4 = 1.   Parabola sideways:  y^2 = 4x.",
            "Triangle: use the Triangle chip under “Shapes by equation”, then change a (half-width) and b (half-height) with the sliders.",
            "Tip: put a, b, c in your equation (x^2 + y^2 = a) and the sliders resize the shape.",
        ),
    ) {
        Pic { m ->
            val h = size.height
            drawCircle(Gold, h * 0.3f, Offset(size.width * 0.2f, h / 2), style = Stroke(5f))
            drawRect(Pink, Offset(size.width * 0.42f, h * 0.2f), Size(h * 0.6f, h * 0.6f), style = Stroke(5f))
            val p = Path().apply {
                moveTo(size.width * 0.8f, h * 0.2f)
                lineTo(size.width * 0.93f, h * 0.8f)
                lineTo(size.width * 0.67f, h * 0.8f)
                close()
            }
            drawPath(p, Green, style = Stroke(5f))
            label(m, "x²+y²=9", size.width * 0.1f, h - 24f, Gold, 11f)
            label(m, "max(|x|,|y|)=3", size.width * 0.38f, h - 24f, Pink, 11f)
        }
    },
    HelpTopic(
        "curves", "✿ ♥  Curves: parametric and polar",
        listOf(
            "Parametric: write both parts with t, in brackets:  (3cos(t), 3sin(t))  draws a circle.",
            "Polar: write r with theta:  r = 4 sin(3 theta)  draws a rose.   r = 0.3 theta  draws a spiral.",
            "The Curves chips have a Heart and a Lissajous figure — press one, then play with the a, b, c sliders.",
            "Tap a curve to select it, like any other figure.",
        ),
    ) {
        Pic { _ ->
            val w = size.width
            val h = size.height
            val rose = Path()
            var first = true
            var t = 0f
            while (t <= PI.toFloat() * 2) {
                val r = sin(3 * t) * h * 0.4f
                val x = w * 0.28f + r * cos(t)
                val y = h / 2 + r * sin(t)
                if (first) {
                    rose.moveTo(x, y)
                    first = false
                } else rose.lineTo(x, y)
                t += 0.03f
            }
            drawPath(rose, Pink, style = Stroke(4f, cap = StrokeCap.Round))
            val heart = Path()
            first = true
            t = 0f
            while (t <= PI.toFloat() * 2) {
                val x = w * 0.72f + 16f * sin(t).let { it * it * it } * (h * 0.0165f)
                val y = h * 0.48f - (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) * (h * 0.0165f)
                if (first) {
                    heart.moveTo(x, y)
                    first = false
                } else heart.lineTo(x, y)
                t += 0.03f
            }
            drawPath(heart, Gold, style = Stroke(4f, cap = StrokeCap.Round))
        }
    },
    HelpTopic(
        "content", "⬚  Content: select, move, resize, colour",
        listOf(
            "Pick the ⬚ Content tool and drag a box round writing, text, pictures or figures.",
            "Tap ✓ to keep what the box caught, or ✗ to forget it.",
            "Drag inside the box to move everything together; drag the small square at the corner to resize.",
            "The bar below gives: copy, cut, delete, paste, crop (one picture), K (neat maths), thicker or thinner pen lines, colours, a size slider and arrows to nudge.",
            "In the Select tool you can also choose ⬚ Content to box several figures — they are then checked for parallel lines.",
        ),
    ) {
        Pic { m ->
            val w = size.width
            val h = size.height
            val dash = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
            val wave = Path().apply {
                moveTo(w * 0.12f, h * 0.5f)
                cubicTo(w * 0.2f, h * 0.2f, w * 0.28f, h * 0.8f, w * 0.36f, h * 0.45f)
            }
            drawPath(wave, Gold, style = Stroke(5f, cap = StrokeCap.Round))
            drawRect(Pink, Offset(w * 0.45f, h * 0.35f), Size(w * 0.18f, h * 0.3f), style = Stroke(4f))
            drawRect(Cream, Offset(w * 0.08f, h * 0.12f), Size(w * 0.6f, h * 0.76f), style = Stroke(3f, pathEffect = dash))
            drawRect(Cream, Offset(w * 0.68f - 9f, h * 0.88f - 9f), Size(18f, 18f))
            drawCircle(Color(0xFF6FA66B), 22f, Offset(w * 0.82f, h * 0.35f))
            label(m, "✓", w * 0.82f - 8f, h * 0.35f - 12f, Color.White, 18f)
            drawCircle(Color(0xFFC0583F), 22f, Offset(w * 0.92f, h * 0.35f))
            label(m, "✗", w * 0.92f - 8f, h * 0.35f - 12f, Color.White, 18f)
        }
    },
    HelpTopic(
        "answer", "🧮  Answer: draw a figure from its sizes",
        listOf(
            "Open 🧮 Answer, pick a figure: triangle, rectangle, quadrilateral, circle, cube, cylinder, cone, graph …",
            "Type the sizes (for a triangle: the three sides) and press Draw ✓.",
            "The figure appears at exactly those sizes with its labels. Write on it with the pen or highlighter, add text, or select it to change colours.",
            "Pick Answer again for the next figure. Undo (↶) removes the last one.",
        ),
    ) {
        Pic { m ->
            val w = size.width
            val h = size.height
            val p = Path().apply {
                moveTo(w * 0.3f, h * 0.8f)
                lineTo(w * 0.7f, h * 0.8f)
                lineTo(w * 0.3f, h * 0.2f)
                close()
            }
            drawPath(p, Gold, style = Stroke(5f))
            label(m, "3", w * 0.22f, h * 0.45f, Cream)
            label(m, "4", w * 0.48f, h * 0.82f, Cream)
            label(m, "5", w * 0.52f, h * 0.42f, Cream)
            label(m, "Triangle 3 · 4 · 5  →  Draw ✓", w * 0.1f, 6f, Copper, 11f)
        }
    },
    HelpTopic(
        "parallel", "∥  Parallel lines and a transversal",
        listOf(
            "Draw two parallel lines and a line that cuts through both. The eight angles ∠1–∠8 appear with their sizes.",
            "Tap an angle to see what it equals. Use the chips for corresponding, alternate and co-interior angles.",
            "A side that only touches the lines at a corner is not a transversal — it has to cut through both lines.",
            "If there is more than one transversal, only one is shown. Double-tap another one to switch to it.",
            "Box several figures with Select → ⬚ Content and tap ✓: the parallel lines inside them are found for you.",
        ),
    ) {
        Pic { m ->
            val w = size.width
            val h = size.height
            drawLine(Gold, Offset(w * 0.1f, h * 0.35f), Offset(w * 0.9f, h * 0.35f), 5f)
            drawLine(Gold, Offset(w * 0.1f, h * 0.7f), Offset(w * 0.9f, h * 0.7f), 5f)
            drawLine(Pink, Offset(w * 0.3f, h * 0.9f), Offset(w * 0.6f, h * 0.1f), 5f)
            label(m, "1", w * 0.5f, h * 0.17f, Cream, 11f)
            label(m, "2", w * 0.57f, h * 0.17f, Cream, 11f)
            label(m, "5", w * 0.4f, h * 0.52f, Cream, 11f)
            label(m, "6", w * 0.47f, h * 0.52f, Cream, 11f)
        }
    },
    HelpTopic(
        "text", "Aa  Text, pasting and K",
        listOf(
            "Aa adds your own text, such as a question. Then move it, resize it (⤢), change its colour and letter size.",
            "Copy text or a picture on your phone, then double-tap empty board (Move or Select tool) → Paste. Triple-tap opens your pictures.",
            "K turns maths written like  x^2 - 1 = 1  or  \\frac{a}{b}  into neat symbols:  x² − 1 = 1,  a/b.",
            "Select the text first (tap it, or use ⬚ Content), then press K.",
        ),
    ) {
        Pic { m ->
            val w = size.width
            label(m, "x^2 − 1 = 1", w * 0.08f, 26f, Cream, 18f)
            label(m, "↓  K", w * 0.08f, 56f, Copper, 16f)
            label(m, "x² − 1 = 1", w * 0.08f, 84f, Gold, 18f)
            label(m, "\\sqrt{x}  →  √x", w * 0.55f, 40f, Cream, 15f)
            label(m, "\\alpha  →  α", w * 0.55f, 70f, Cream, 15f)
        }
    },
    HelpTopic(
        "pen", "🖊️  Pen, highlighter and stylus",
        listOf(
            "Tap 🖊️ Write: pick Pen, Highlighter or Eraser, then colours, width and (for the pen) solid, dashed or dotted lines.",
            "Lines holds the straight line and points; Shapes, 3D and Graph hold everything else.",
            "A stylus works too: resting your palm while writing with a stylus is ignored, the stylus' eraser end erases, and fast strokes stay smooth.",
            "If a home-made soft-tip pen is not noticed at all, the phone itself is not reporting it as a touch — try it in another app first.",
        ),
    ) {
        Pic { m ->
            val w = size.width
            val h = size.height
            drawLine(Gold, Offset(w * 0.1f, h * 0.3f), Offset(w * 0.9f, h * 0.3f), 8f, StrokeCap.Round)
            drawLine(Pink, Offset(w * 0.1f, h * 0.55f), Offset(w * 0.9f, h * 0.55f), 8f, StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f)))
            drawLine(Green, Offset(w * 0.1f, h * 0.8f), Offset(w * 0.9f, h * 0.8f), 8f, StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(0.1f, 20f)))
            label(m, "solid", w * 0.75f, h * 0.12f, Cream, 11f)
        }
    },
)

/** The “?” page: a picture for each feature with how to use it. */
@Composable
fun HelpDialog(startTopic: String, onClose: () -> Unit) {
    val list = rememberLazyListState()
    LaunchedEffect(startTopic) {
        val i = TOPICS.indexOfFirst { it.id == startTopic }
        if (i > 0) list.scrollToItem(i)
    }
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF1A110C))
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 12.dp),
        ) {
            Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "How to use",
                    color = Cream,
                    fontSize = 20.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                IconBtn("✕", compact = true) { onClose() }
            }
            LazyColumn(Modifier.weight(1f), state = list, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(TOPICS) { t ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF2A1B12))
                            .border(BorderStroke(1.dp, Color(0xFF6B4630)), RoundedCornerShape(16.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(t.title, color = Gold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        t.pic()
                        t.lines.forEachIndexed { i, s ->
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("${i + 1}.", color = Copper, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(s, color = Color(0xFFE9D8C4), fontSize = 12.sp, lineHeight = 17.sp)
                            }
                        }
                    }
                }
                item { Box(Modifier.height(24.dp)) }
            }
        }
    }
}
