package com.example.data

import org.json.JSONArray
import org.json.JSONObject

data class DoodlePoint(
    val x: Float,
    val y: Float
)

data class DoodleStroke(
    val points: List<DoodlePoint>,
    val colorHex: String,
    val strokeWidth: Float
)

data class DoodleStamp(
    val x: Float,
    val y: Float,
    val symbol: String,
    val size: Float = 28f
)

data class DoodleDrawing(
    val strokes: List<DoodleStroke> = emptyList(),
    val stamps: List<DoodleStamp> = emptyList(),
    val bgColorHex: String = "#16152B"
) {
    fun toJson(): String {
        return try {
            val root = JSONObject()
            root.put("bg", bgColorHex)

            val strokesArr = JSONArray()
            for (stroke in strokes) {
                val sObj = JSONObject()
                sObj.put("c", stroke.colorHex)
                sObj.put("w", stroke.strokeWidth.toDouble())
                val ptsArr = JSONArray()
                for (pt in stroke.points) {
                    val pObj = JSONObject()
                    pObj.put("x", pt.x.toDouble())
                    pObj.put("y", pt.y.toDouble())
                    ptsArr.put(pObj)
                }
                sObj.put("pts", ptsArr)
                strokesArr.put(sObj)
            }
            root.put("strokes", strokesArr)

            val stampsArr = JSONArray()
            for (stamp in stamps) {
                val stObj = JSONObject()
                stObj.put("x", stamp.x.toDouble())
                stObj.put("y", stamp.y.toDouble())
                stObj.put("sym", stamp.symbol)
                stObj.put("sz", stamp.size.toDouble())
                stampsArr.put(stObj)
            }
            root.put("stamps", stampsArr)

            root.toString()
        } catch (_: Exception) {
            "{}"
        }
    }

    companion object {
        fun fromJson(jsonStr: String?): DoodleDrawing {
            if (jsonStr.isNullOrBlank() || jsonStr == "{}") return DoodleDrawing()
            return try {
                val root = JSONObject(jsonStr)
                val bg = root.optString("bg", "#16152B")

                val strokeList = mutableListOf<DoodleStroke>()
                val strokesArr = root.optJSONArray("strokes")
                if (strokesArr != null) {
                    for (i in 0 until strokesArr.length()) {
                        val sObj = strokesArr.getJSONObject(i)
                        val color = sObj.optString("c", "#FFFFFF")
                        val width = sObj.optDouble("w", 5.0).toFloat()
                        val ptsArr = sObj.optJSONArray("pts")
                        val pts = mutableListOf<DoodlePoint>()
                        if (ptsArr != null) {
                            for (j in 0 until ptsArr.length()) {
                                val pObj = ptsArr.getJSONObject(j)
                                pts.add(
                                    DoodlePoint(
                                        pObj.optDouble("x", 0.0).toFloat(),
                                        pObj.optDouble("y", 0.0).toFloat()
                                    )
                                )
                            }
                        }
                        strokeList.add(DoodleStroke(pts, color, width))
                    }
                }

                val stampList = mutableListOf<DoodleStamp>()
                val stampsArr = root.optJSONArray("stamps")
                if (stampsArr != null) {
                    for (i in 0 until stampsArr.length()) {
                        val stObj = stampsArr.getJSONObject(i)
                        stampList.add(
                            DoodleStamp(
                                x = stObj.optDouble("x", 0.0).toFloat(),
                                y = stObj.optDouble("y", 0.0).toFloat(),
                                symbol = stObj.optString("sym", "⭐"),
                                size = stObj.optDouble("sz", 28.0).toFloat()
                            )
                        )
                    }
                }

                DoodleDrawing(strokeList, stampList, bg)
            } catch (_: Exception) {
                DoodleDrawing()
            }
        }

        fun createSampleCake(): DoodleDrawing {
            // Preset doodle of a birthday cake with candles
            val strokes = listOf(
                // Base cake tier
                DoodleStroke(
                    listOf(
                        DoodlePoint(40f, 150f),
                        DoodlePoint(160f, 150f),
                        DoodlePoint(155f, 180f),
                        DoodlePoint(45f, 180f),
                        DoodlePoint(40f, 150f)
                    ),
                    "#F472B6", // Pink
                    8f
                ),
                // Top cake tier
                DoodleStroke(
                    listOf(
                        DoodlePoint(60f, 120f),
                        DoodlePoint(140f, 120f),
                        DoodlePoint(140f, 150f),
                        DoodlePoint(60f, 150f),
                        DoodlePoint(60f, 120f)
                    ),
                    "#60A5FA", // Sky Blue
                    8f
                ),
                // Candle
                DoodleStroke(
                    listOf(DoodlePoint(100f, 120f), DoodlePoint(100f, 95f)),
                    "#FBBF24", // Gold
                    6f
                ),
                // Flame
                DoodleStroke(
                    listOf(DoodlePoint(100f, 92f), DoodlePoint(100f, 85f)),
                    "#EF4444", // Red flame
                    10f
                )
            )
            val stamps = listOf(
                DoodleStamp(100f, 82f, "✨", 22f),
                DoodleStamp(35f, 125f, "🎂", 24f),
                DoodleStamp(165f, 125f, "🎉", 24f)
            )
            return DoodleDrawing(strokes, stamps, "#1E1A38")
        }

        fun createSampleBeach(): DoodleDrawing {
            // Preset doodle of sun & ocean waves
            val strokes = listOf(
                // Sun
                DoodleStroke(
                    listOf(
                        DoodlePoint(60f, 60f),
                        DoodlePoint(75f, 50f),
                        DoodlePoint(90f, 60f),
                        DoodlePoint(85f, 75f),
                        DoodlePoint(65f, 75f),
                        DoodlePoint(60f, 60f)
                    ),
                    "#F59E0B", // Sun Amber
                    10f
                ),
                // Waves
                DoodleStroke(
                    listOf(
                        DoodlePoint(20f, 150f),
                        DoodlePoint(50f, 140f),
                        DoodlePoint(80f, 150f),
                        DoodlePoint(110f, 140f),
                        DoodlePoint(140f, 150f),
                        DoodlePoint(180f, 140f)
                    ),
                    "#38BDF8", // Cyan wave
                    7f
                ),
                DoodleStroke(
                    listOf(
                        DoodlePoint(20f, 170f),
                        DoodlePoint(60f, 160f),
                        DoodlePoint(100f, 170f),
                        DoodlePoint(140f, 160f),
                        DoodlePoint(180f, 170f)
                    ),
                    "#0284C7", // Deeper wave
                    7f
                )
            )
            val stamps = listOf(
                DoodleStamp(160f, 60f, "🏖️", 26f),
                DoodleStamp(130f, 80f, "✈️", 22f)
            )
            return DoodleDrawing(strokes, stamps, "#0F172A")
        }

        fun createSampleGraduation(): DoodleDrawing {
            val strokes = listOf(
                // Trophy/Star outline
                DoodleStroke(
                    listOf(
                        DoodlePoint(100f, 40f),
                        DoodlePoint(115f, 80f),
                        DoodlePoint(160f, 80f),
                        DoodlePoint(125f, 105f),
                        DoodlePoint(140f, 150f),
                        DoodlePoint(100f, 125f),
                        DoodlePoint(60f, 150f),
                        DoodlePoint(75f, 105f),
                        DoodlePoint(40f, 80f),
                        DoodlePoint(85f, 80f),
                        DoodlePoint(100f, 40f)
                    ),
                    "#FBBF24",
                    7f
                )
            )
            val stamps = listOf(
                DoodleStamp(100f, 100f, "🎓", 32f),
                DoodleStamp(50f, 160f, "🏆", 26f),
                DoodleStamp(150f, 160f, "🌟", 26f)
            )
            return DoodleDrawing(strokes, stamps, "#1A1A3A")
        }
    }
}
