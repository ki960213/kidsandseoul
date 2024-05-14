package com.ki960213.kidsandseoul.data.repository

import com.ki960213.domain.administrativedong.repository.AdministrativeDongRepository
import com.ki960213.domain.auth.repository.AuthRepository
import com.ki960213.domain.comment.repository.CommentRepository
import com.ki960213.domain.facility.repository.FacilityRepository
import com.ki960213.domain.kid.repository.KidRepository
import com.ki960213.domain.post.repository.PostRepository
import com.ki960213.domain.review.repository.ReviewRepository
import com.ki960213.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsAuthRepository(authRepository: DefaultAuthRepository): AuthRepository

    @Binds
    abstract fun bindsAdministrativeDongRepository(
        administrativeDongRepository: DefaultAdministrativeDongRepository,
    ): AdministrativeDongRepository

    @Binds
    abstract fun bindsCommentRepository(commentRepository: DefaultCommentRepository): CommentRepository

    @Binds
    abstract fun bindsFacilityRepository(facilityRepository: DefaultFacilityRepository): FacilityRepository

    @Binds
    abstract fun bindsPostRepository(postRepository: DefaultPostRepository): PostRepository

    @Binds
    abstract fun bindsReviewRepository(reviewRepository: DefaultReviewRepository): ReviewRepository

    @Binds
    abstract fun bindsUserRepository(userRepository: DefaultUserRepository): UserRepository

    @Binds
    abstract fun bindsKidRepository(kidRepository: DefaultKidRepository): KidRepository
}
