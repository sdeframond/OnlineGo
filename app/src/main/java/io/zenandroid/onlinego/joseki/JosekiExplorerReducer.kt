package io.zenandroid.onlinego.joseki

import io.zenandroid.onlinego.joseki.JosekiExplorerAction.*
import io.zenandroid.onlinego.model.Position
import io.zenandroid.onlinego.model.ogs.JosekiPosition
import io.zenandroid.onlinego.mvi.Reducer

class JosekiExplorerReducer : Reducer<JosekiExplorerState, JosekiExplorerAction> {
    override fun reduce(state: JosekiExplorerState, action: JosekiExplorerAction): JosekiExplorerState {
        return when (action) {
            is PositionLoaded ->
                if(state.lastRequestedNodeId == null || state.lastRequestedNodeId == action.position.node_id) {
                    val history = if(state.position != null) state.historyStack + state.position else state.historyStack
                    state.copy(
                            position = action.position,
                            description = descriptionOfPosition(action.position),
                            boardPosition = Position.fromJosekiPosition(action.position),
                            historyStack = history,
                            loading = false,
                            candidateMove = null,
                            error = null,
                            backButtonEnabled = history.isNotEmpty(),
                            passButtonEnabled = action.position.next_moves?.find { it.placement == "pass" } != null
                    )
                } else {
                    state
                }

            is StartDataLoading -> state.copy(
                    loading = true,
                    lastRequestedNodeId = action.id,
                    backButtonEnabled = false
            )

            is ShowCandidateMove -> state.copy(
                    candidateMove = action.placement
            )

            is DataLoadingError -> state.copy(
                    loading = false,
                    error = action.e
            )

            Finish -> state.copy(
                    shouldFinish = true
            )

            UserPressedBack -> {
                if(state.historyStack.isEmpty()) {
                    state.copy(shouldFinish = true)
                } else {
                    val history = state.historyStack.dropLast(1)
                    val position = state.historyStack.last()
                    state.copy(
                            position = position,
                            description = descriptionOfPosition(position),
                            boardPosition = Position.fromJosekiPosition(state.historyStack.last()),
                            historyStack = history,
                            loading = false,
                            candidateMove = null,
                            error = null,
                            backButtonEnabled = history.isNotEmpty(),
                            passButtonEnabled = position.next_moves?.find { it.placement == "pass" } != null
                    )
                }
            }

            is UserTappedCoordinate,
            is LoadPosition,
            is UserHotTrackedCoordinate,
            UserPressedPass,
            ViewReady
            -> state
        }
    }

    private fun descriptionOfPosition(pos: JosekiPosition?): String? {
        return if(pos == null || pos.placement == "root") {
            "## Joseki Explorer\n" +
                    "*Joseki* is an English loanword from Japanese, usually referring to " +
                    "standard sequences of moves played out in a corner that result in a locally even exchange.\n" +
                    "### How to use the Joseki Explorer\n" +
                    "The marked moves below represent interesting continuations to the current position. " +
                    "The colours represent how good the move is considered to be: \n" +
                    "* Green moves are considered optimal\n" +
                    "* Yellow moves are considered OK\n" +
                    "* Red moves are considered mistakes\n" +
                    "* Purple markers are for trick plays\n" +
                    "* Blue for open questions.\n" +
                    "You can tap any of these interesting moves to explore the positions they lead to.\n" +
                    "### Tenuki\n" +
                    "Sometimes the best move is to play somewhere else. This is normally referred to as " +
                    "*tenuki*. If tenuki is considered an interesting option for the current position the " +
                    "pass button in the bottom left is enabled and you can press it to see what positions may " +
                    "arise after the current player tenukis."
        } else {
            pos.description
        }
    }
}