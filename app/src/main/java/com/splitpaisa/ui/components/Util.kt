package com.splitpaisa.ui.components

fun fuzzyScore(query: String, target: String): Int {
    if (query.isBlank()) return -9999
    val q = query.lowercase()
    val t = target.lowercase()
    if (t.startsWith(q)) return 1000 - (t.length - q.length)
    var qi = 0; var score = 0
    for (c in t) {
        if (qi < q.length && c == q[qi]) { score += 10; qi++ }
    }
    if (t.contains(q)) score += 50
    return score - kotlin.math.max(0, t.length - q.length)
}
