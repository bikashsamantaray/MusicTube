package com.plcoding.spotifycloneyt.ui.viewModels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plcoding.spotifycloneyt.data.entities.Songs
import com.plcoding.spotifycloneyt.exoplayer.MusicServiceConnection
import com.plcoding.spotifycloneyt.exoplayer.isPlayEnabled
import com.plcoding.spotifycloneyt.exoplayer.isPlaying
import com.plcoding.spotifycloneyt.exoplayer.isPrepared
import com.plcoding.spotifycloneyt.other.Constants.MEDIA_ROOT_ID
import com.plcoding.spotifycloneyt.other.Resources
import java.text.FieldPosition

class MainViewModel @ViewModelInject constructor(
    private val musicServiceConnection: MusicServiceConnection
): ViewModel() {
    private val _mediaItems = MutableLiveData<Resources<List<Songs>>>()
    val mediaItems : LiveData<Resources<List<Songs>>> = _mediaItems

    val isConnected = musicServiceConnection.isConnected
    val netWorkError = musicServiceConnection.networkError
    val curPlayingSongs = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState


    init {
        _mediaItems.postValue(Resources.loading(null))
        musicServiceConnection.subscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Songs(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }

                _mediaItems.postValue(Resources.success(items))
            }
        })
    }

    fun skipToNextSong(){
        musicServiceConnection.transPortControls.skipToNext()
    }

    fun skipToPrevious(){
        musicServiceConnection.transPortControls.skipToPrevious()
    }

    fun seekTo(position: Long){
        musicServiceConnection.transPortControls.seekTo(position)
    }

    fun playOrToggleSong(mediaItem: Songs, toggle: Boolean = false){
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId == curPlayingSongs.value?.getString(METADATA_KEY_MEDIA_ID)){
            playbackState.value?.let { playbackState ->
                when{
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transPortControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transPortControls.play()
                    else -> Unit
                }
            }
        }else{
            musicServiceConnection.transPortControls.playFromMediaId(mediaItem.mediaId, null)
        }

    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object :MediaBrowserCompat.SubscriptionCallback(){

        })
    }


}