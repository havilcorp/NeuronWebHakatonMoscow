package com.mvp.myapplication.data.models.api

class Requests {

    // GET

    data class Tsrs(
            var status: String,
            var message: String,
            var response: List<ModelTsr>
    )

    // POST

    data class PostGetObjects(
            var status: String,
            var message: String
    )

    data class GetObjectSend(
        var requests: List<GetObjectSend_requests>
    )

    data class GetObjectSend_requests(
        var features: List<GetObjectSend_requests_features>,
        var image: GetObjectSend_image
    )

    data class GetObjectSend_requests_features(
        var maxResults: Int,
        var type: String
    )

    data class GetObjectSend_image(
        var content: String
    )





    data class OBJECT_LOCALIZATION(
        val responses: List<OBJECT_LOCALIZATION_Responses>
    )

    data class OBJECT_LOCALIZATION_Responses(
        val localizedObjectAnnotations: List<OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations>
    )

    data class OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations(
        val mid: String,
        val name: String,
        val score: Float,
        val boundingPoly: OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly
    )

    data class OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly(
        val normalizedVertices: List<OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly_NormalizedVertices>
    )

    data class OBJECT_LOCALIZATION_Responses_LocalizedObjectAnnotations_BoundingPoly_NormalizedVertices(
        val x: Float,
        val y: Float
    )

}

