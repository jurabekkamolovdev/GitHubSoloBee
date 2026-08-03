package com.javohir.data.mapper.topic

/**
 * Created by: Javohir Oromov macos
 * Project: SoloBee
 * Package: com.javohir.data.mapper.topic
 * Description: TopicsMapper: network modeldan domain mapping.
 */

import com.javohir.data.model.response.topic.TopicData
import com.javohir.data.model.response.topic.TopicsResponse
import com.javohir.domain.model.Topic

fun TopicsResponse.toDomain(): List<Topic> {
    return data.map { it.toDomain() }
}

fun TopicData.toDomain(): Topic {
    return Topic(
        id = id,
        subCategoryId = subCategoryId,
        imageUrl = imageUrl,
        enabled = enabled,
        completed = completed,
    )
}
