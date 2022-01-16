package de.pfannkuchen.lotas.videorecorder;

import java.nio.ByteBuffer;

import de.pfannkuchen.lotas.util.LoTASHelper;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

/**
 * A serializable sound with a timestamp given in frames.
 * @author Pancake
 */
public class SoundWithTimestamp implements SoundInstance {

	// The position of the sound
	public double x;
	public double y;
	public double z;
	// The pitch and volume of the sound
	public float pitch = 1.0f;
	public float volume = 1.0f;
	// The Resource location of the sound
	public String namespace;
	public String path;
	// More options
	public boolean relative;
	public boolean looping;
	public int delay;
	public int attenuation;
	public int source;
	// Frame of occurence
	public int frame;
	
	/**
	 * Creates a new serializable sound using a given sound instance
	 * @param sound Based sound instance
	 */
	public SoundWithTimestamp(SoundInstance sound, int frame) {
		this.x = sound.getX();
		this.y = sound.getY();
		this.z = sound.getZ();
		this.namespace = sound.getLocation().getNamespace();
		this.path = sound.getLocation().getPath();
		if (sound.getSound() != null) {
			this.pitch = sound.getPitch();
			this.volume = sound.getVolume();
		}
		this.relative = sound.isRelative();
		this.looping = sound.isLooping();
		this.delay = sound.getDelay();
		this.attenuation = sound.getAttenuation().ordinal();
		this.source = sound.getSource().ordinal();
		this.frame = frame;
	}
	
	/**
	 * Creates a new serializable sound using a given byte buffer
	 * @param buf Based byte buffer
	 */
	public SoundWithTimestamp(ByteBuffer buf) {
		this.x = buf.getDouble();
		this.y = buf.getDouble();
		this.z = buf.getDouble();
		this.pitch = buf.getFloat();
		this.volume = buf.getFloat();
		this.namespace = LoTASHelper.readString(buf);
		this.path = LoTASHelper.readString(buf);
		this.relative = (buf.get() == ((byte) 1)) ? true : false;
		this.looping = (buf.get() == ((byte) 1)) ? true : false;
		this.delay = buf.getInt();
		this.attenuation = buf.getInt();
		this.source = buf.getInt();
		this.frame = buf.getInt();
	}
	
	/**
	 * Serializes the sound to a byte buffer
	 * @param buf Output buffer
	 */
	public void write(ByteBuffer buf) {
		buf.putDouble(this.x);
		buf.putDouble(this.y);
		buf.putDouble(this.z);
		buf.putFloat(this.pitch);
		buf.putFloat(this.volume);
		LoTASHelper.writeString(buf, this.namespace);
		LoTASHelper.writeString(buf, this.path);
		buf.put(this.relative ? ((byte) 1) : ((byte) 0));
		buf.put(this.looping ? ((byte) 1) : ((byte) 0));
		buf.putInt(this.delay);
		buf.putInt(this.attenuation);
		buf.putInt(this.source);
		buf.putInt(this.frame);
	}

	// Below this point are the rewrites for the interface
	
	@Override
	public ResourceLocation getLocation() {
		return new ResourceLocation(this.namespace, this.path);
	}

	@Override
	public WeighedSoundEvents resolve(SoundManager sm) {
		return sm.getSoundEvent(getLocation());
	}

	@Override
	public Sound getSound() {
		return SoundManager.EMPTY_SOUND;
	}

	@Override
	public SoundSource getSource() {
		return SoundSource.values()[source];
	}

	@Override
	public boolean isLooping() {
		return this.looping;
	}

	@Override
	public boolean isRelative() {
		return this.relative;
	}

	@Override
	public int getDelay() {
		return this.delay;
	}

	@Override
	public float getVolume() {
		return this.volume;
	}

	@Override
	public float getPitch() {
		return this.pitch;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public Attenuation getAttenuation() {
		return Attenuation.values()[attenuation];
	}
	
}
