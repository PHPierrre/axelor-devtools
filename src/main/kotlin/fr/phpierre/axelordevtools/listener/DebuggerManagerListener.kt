package fr.phpierre.axelordevtools.listener

import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManagerListener

class DebuggerManagerListener : XDebuggerManagerListener {

    override fun processStarted(debugProcess: XDebugProcess) {
        super.processStarted(debugProcess)
        debugProcess.session.addSessionListener(DebuggerListener());
    }

    override fun processStopped(debugProcess: XDebugProcess) {
        super.processStopped(debugProcess)
    }

    override fun currentSessionChanged(previousSession: XDebugSession?, currentSession: XDebugSession?) {
        super.currentSessionChanged(previousSession, currentSession)
    }
}