package com.gitsoft.thoughtpad.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TasksWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget = TasksWidget()
}