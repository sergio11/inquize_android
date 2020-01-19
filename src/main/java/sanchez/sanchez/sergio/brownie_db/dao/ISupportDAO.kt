package sanchez.sanchez.sergio.brownie_db.dao

import androidx.room.*

/**
 * Support DAO Interface
 */
interface ISupportDAO<T> {

    /**
     * Insert
     * @param entity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: T): LongArray

    /**
     * Insert an object in the database.
     *
     * @param entity the object to be inserted.
     * @return The SQLite row id
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param entityList the objects to be inserted.
     * @return The SQLite row ids
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entityList: List<T>): List<Long>


    /**
     * Update an object from the database.
     *
     * @param entity the object to be updated
     */
    @Update
    fun update(entity: T)


    /**
     * Update an array of objects from the database.
     *
     * @param entityList the object to be updated
     */
    @Update
    fun update(entityList: List<T>)

    /**
     * Delete
     * @param entity
     */
    @Delete
    fun delete(entity: T)

    /**
     * Find All
     */
    fun findAll(): List<T>

    /**
     * Count
     */
    fun count(): Int

    /**
     *
     */
    @Transaction
    fun upsert(entity: T)

    /**
     *
     */
    @Transaction
    fun upsert(entityList: List<T>)
}