package com.dreamsoftware.inquize.di

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IImageDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IInquizeDataSource
import com.dreamsoftware.inquize.data.remote.datasource.impl.AuthRemoteDataSourceImpl
import com.dreamsoftware.inquize.data.remote.datasource.impl.ImageDataSourceImpl
import com.dreamsoftware.inquize.data.remote.datasource.impl.InquizeDataSourceImpl
import com.dreamsoftware.inquize.data.remote.dto.AuthUserDTO
import com.dreamsoftware.inquize.data.remote.dto.CreateInquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.mapper.CreateInquizeRemoteMapper
import com.dreamsoftware.inquize.data.remote.mapper.UserAuthenticatedMapper
import com.dreamsoftware.inquize.data.remote.mapper.InquizeRemoteMapper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

// Dagger module for providing Firebase-related dependencies
@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    /**
     * Provides a singleton instance of UserAuthenticatedMapper.
     * @return a new instance of UserAuthenticatedMapper.
     */
    @Provides
    @Singleton
    fun provideUserAuthenticatedMapper(): IBrownieOneSideMapper<FirebaseUser, AuthUserDTO> = UserAuthenticatedMapper()

    @Provides
    @Singleton
    fun provideUserQuestionRemoteMapper(): IBrownieOneSideMapper<Map<String, Any?>, InquizeDTO> = InquizeRemoteMapper()

    @Provides
    @Singleton
    fun provideSaveUserQuestionRemoteMapper(): IBrownieOneSideMapper<CreateInquizeDTO, Map<String, Any?>> = CreateInquizeRemoteMapper()

    /**
     * Provides a singleton instance of FirebaseAuth.
     * @return the default instance of FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Provide Firebase Store
     */
    @Provides
    @Singleton
    fun provideFirebaseStore() = Firebase.firestore

    /**
     * Provide Firebase Storage
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    /**
     * Provides a singleton instance of IAuthDataSource.
     * @param userAuthenticatedMapper the IBrownieOneSideMapper<FirebaseUser, AuthUserDTO> instance.
     * @param firebaseAuth the FirebaseAuth instance.
     * @return a new instance of AuthDataSourceImpl implementing IAuthDataSource.
     */
    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        userAuthenticatedMapper: IBrownieOneSideMapper<FirebaseUser, AuthUserDTO>,
        firebaseAuth: FirebaseAuth
    ): IAuthRemoteDataSource = AuthRemoteDataSourceImpl(
        userAuthenticatedMapper,
        firebaseAuth
    )

    @Provides
    @Singleton
    fun provideUserPicturesDataSource(
        storage: FirebaseStorage,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IImageDataSource = ImageDataSourceImpl(
        storage,
        dispatcher
    )

    @Provides
    @Singleton
    fun provideInquizeDataSource(
        firestore: FirebaseFirestore,
        saveUserQuestionMapper: IBrownieOneSideMapper<CreateInquizeDTO, Map<String, Any?>>,
        userQuestionMapper: IBrownieOneSideMapper<Map<String, Any?>, InquizeDTO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IInquizeDataSource = InquizeDataSourceImpl(
        firestore,
        saveUserQuestionMapper,
        userQuestionMapper,
        dispatcher
    )
}