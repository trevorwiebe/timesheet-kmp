package com.trevorwiebe.timesheet.core.data

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion

// Central registry for Firestore listeners
object FirestoreListenerRegistry {
    private val activeFlows = mutableListOf<Flow<*>>()
    private val activeJobs = mutableListOf<Job>()

    fun <T> registerFlow(flow: Flow<T>): Flow<T> {
        activeFlows.add(flow)
        return flow.onCompletion {
            activeFlows.remove(flow)
        }
    }

    fun registerJob(job: Job) {
        activeJobs.add(job)
    }

    fun removeJob(job: Job) {
        activeJobs.remove(job)
    }

    fun clearAllListeners() {
        activeJobs.forEach { it.cancel() }
        activeJobs.clear()
    }
}