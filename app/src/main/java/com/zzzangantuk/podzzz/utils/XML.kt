package com.zzzangantuk.podzzz.utils

import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlConfig.Companion.IGNORING_UNKNOWN_CHILD_HANDLER

@OptIn(ExperimentalXmlUtilApi::class)
val xml = XML.recommended_1_0 {
    repairNamespaces = true

    policy {
        autoPolymorphic = true
        unknownChildHandler = IGNORING_UNKNOWN_CHILD_HANDLER
    }
}