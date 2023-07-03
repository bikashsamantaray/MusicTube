package com.plcoding.spotifycloneyt.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.audio.DefaultAudioSink
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.plcoding.spotifycloneyt.data.remote.MusicDatabase
import com.plcoding.spotifycloneyt.exoplayer.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {


    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = MusicServiceConnection(context)


    @ServiceScoped
    @Provides
    fun provideMusicDatabase() = MusicDatabase()

    @ServiceScoped
    @Provides
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @ServiceScoped
    @Provides
    fun provideExoPlayers(
        @ApplicationContext context:Context,
        audioAttributes: AudioAttributes
    ) = SimpleExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)
    }

   @ServiceScoped
   @Provides
   fun provideDataSourceFactory(
       @ApplicationContext context: Context
   ) = DefaultDataSourceFactory(context, Util.getUserAgent(context, "Music Tube App"))
}