package fr.phpierre.axelordevtools.listener

import com.intellij.xdebugger.XDebugSessionListener

class DebuggerListener : XDebugSessionListener {

    companion object {
        var isPause = false
    }

    override fun sessionPaused() {
        super.sessionPaused()
        isPause = true;
    }

    override fun sessionResumed() {
        super.sessionResumed()
        isPause = false;
    }

    override fun sessionStopped() {
        super.sessionStopped()
        isPause = false;
    }
}