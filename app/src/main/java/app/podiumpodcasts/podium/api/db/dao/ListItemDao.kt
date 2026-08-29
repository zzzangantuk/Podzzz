package app.podiumpodcasts.podium.api.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.podiumpodcasts.podium.api.db.model.ListItemModel
import app.podiumpodcasts.podium.api.db.model.ListItemModelBundle
import kotlinx.coroutines.flow.Flow

@Dao
interface ListItemDao {

    @Transaction
    @Query("SELECT * FROM listItem WHERE listId = :listId ORDER BY position ASC")
    fun all(listId: Int): PagingSource<Int, ListItemModelBundle>

    @Query("SELECT * FROM listItem WHERE listId = :listId AND contentId = :contentId")
    suspend fun get(listId: Int, contentId: String): ListItemModel

    @Query("SELECT EXISTS(SELECT 1 FROM listItem WHERE contentId = :contentId AND listId != -2)")
    fun exists(contentId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM listItem WHERE contentId = :contentId AND listId = -2)")
    fun isFavorite(contentId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM listItem WHERE contentId = :contentId AND listId = -1)")
    fun isOnHearLaterFlow(contentId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM listItem WHERE contentId = :contentId AND listId = -1)")
    suspend fun isOnHearLater(contentId: String): Boolean

    @Transaction
    suspend fun addListItemAndRefreshItemCount(
        listId: Int,
        contentId: String,
        isPodcast: Boolean,
        position: Int,
    ): Int {
        val id = addListItem(listId, contentId, isPodcast, position)
        refreshItemCount(listId)

        if(position < 4) updateCover(listId)
        return id.toInt()
    }

    @Transaction
    suspend fun deleteAndReindex(listId: Int, itemId: Int, deletedPosition: Int) {
        deleteById(itemId)
        shiftPositionsDown(listId, deletedPosition)
        refreshItemCount(listId)

        if(deletedPosition < 4) updateCover(listId)
    }

    @Transaction
    suspend fun moveAndReindex(listId: Int, itemId: Int, fromPos: Int, toPos: Int) {
        when(fromPos < toPos) {
            true -> shiftItemsUp(listId, fromPos, toPos)
            false -> shiftItemsDown(listId, toPos, fromPos)
        }

        updatePosition(itemId, toPos)
        if ((fromPos < 4) || (toPos < 4)) updateCover(listId)
    }

    @Query("SELECT MAX(position) + 1 FROM listItem WHERE listId = :listId")
    suspend fun getNextPosition(listId: Int): Int?

    @Query("INSERT INTO listItem (listId, contentId, isPodcast, position) VALUES (:listId, :contentId, :isPodcast, :position)")
    suspend fun addListItem(
        listId: Int,
        contentId: String,
        isPodcast: Boolean,
        position: Int,
    ): Long

    @Query("DELETE FROM listItem WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query(
        """
        UPDATE listItem 
        SET position = position - 1 
        WHERE listId = :listId AND position > :deletedPosition
    """,
    )
    suspend fun shiftPositionsDown(listId: Int, deletedPosition: Int)

    @Query("UPDATE listItem SET position = position - 1 WHERE listId = :listId AND position > :from AND position <= :to")
    suspend fun shiftItemsUp(listId: Int, from: Int, to: Int)

    @Query("UPDATE listItem SET position = position + 1 WHERE listId = :listId AND position >= :to AND position < :from")
    suspend fun shiftItemsDown(listId: Int, to: Int, from: Int)

    @Query("UPDATE listItem SET position = :newPos WHERE id = :itemId")
    suspend fun updatePosition(itemId: Int, newPos: Int)

    @Query(
        """
        UPDATE list 
        SET itemCount = (SELECT COUNT(*) FROM listItem WHERE listId = :listId) 
        WHERE id = :listId
    """,
    )
    suspend fun refreshItemCount(listId: Int)

    @Transaction
    suspend fun updateCover(listId: Int) {
        val coverItems = getCoverItems(listId)
        val imageUrls = coverItems.asSequence()
            .mapNotNull { (it.episode)?.imageUrl ?: it.podcast?.imageUrl }
            .joinToString(separator = "\n")
        updateListImageUrls(listId, imageUrls)
    }

    @Query("SELECT id FROM listItem WHERE listId = :listId AND position = :position LIMIT 1")
    suspend fun getItemIdAtPosition(listId: Int, position: Int): Int?

    @Transaction
    @Query("SELECT * FROM listItem WHERE listId = :listId ORDER BY position LIMIT 4")
    suspend fun getCoverItems(listId: Int): List<ListItemModelBundle>

    @Query("UPDATE list SET imageUrls = :imageUrls WHERE id = :listId")
    suspend fun updateListImageUrls(listId: Int, imageUrls: String)

}