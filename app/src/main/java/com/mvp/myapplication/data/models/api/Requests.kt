package com.mvp.myapplication.data.models.api

class Requests {

    data class GetObjectSend (
        var requests: List<GetObjectSend_requests>
    )

    data class GetObjectSend_requests (
        var features: List<GetObjectSend_requests_features>,
        var image: GetObjectSend_image
    )

    data class GetObjectSend_requests_features (
        var maxResults: Int,
        var type: String
    )

    data class GetObjectSend_image (
        var content: String
    )


    data class OBJECT_LOCALIZATION (
        val responses: List<OBJECT_LOCALIZATION_Responses>
    )

    data class OBJECT_LOCALIZATION_Responses(
        val localizedObjectAnnotations: List<OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations>
    )

    data class OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations (
        val mid: String,
        val name: String,
        val score: Float,
        val boundingPoly: OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly
    )

    data class OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly (
        val normalizedVertices: List<OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly_NormalizedVertices>
    )

    data class OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly_NormalizedVertices (
        val x: Float,
        val y: Float
    )




    data class OBJECT_DETECTION (
        val responses: List<OBJECT_DETECTION_Responses>
    )
    data class OBJECT_DETECTION_Responses (
        val labelAnnotations: List<OBJECT_DETECTION_Responses_LabelAnnotations>,
        val imagePropertiesAnnotation: OBJECT_DETECTION_Responses_ImagePropertiesAnnotation
    )
    data class OBJECT_DETECTION_Responses_LabelAnnotations (
        val mid: String,
        val description: String,
        val score: Float,
        val topicality: Float
    )
    data class OBJECT_DETECTION_Responses_ImagePropertiesAnnotation(
        val dominantColors: OBJECT_DETECTION_Responses_ImagePropertiesAnnotation_DominantColors
    )
    data class OBJECT_DETECTION_Responses_ImagePropertiesAnnotation_DominantColors(
        val colors: List<OBJECT_DETECTION_Responses_ImagePropertiesAnnotation_DominantColors_Colors>
    )
    data class OBJECT_DETECTION_Responses_ImagePropertiesAnnotation_DominantColors_Colors (
        val color: OBJECT_DETECTION_Responses_ImagePropertiesAnnotation_DominantColors_Colors_Color,
        val score: Float
    )
    data class OBJECT_DETECTION_Responses_ImagePropertiesAnnotation_DominantColors_Colors_Color (
        val red: Int,
        val green: Int,
        val blue: Int
    )

}

