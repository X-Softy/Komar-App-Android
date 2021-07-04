package com.xsofty.shared

inline val <T : Any> T?.notNull: T
    get() = requireNotNull(this)