// class for generating different ugen subgraphs to stick in thebangs

Bangs {

	// the classic polyperc sound.
	*square {
		arg freq, pw, cutoff, gain, env;
		postln(freq, pw, cutoff, gain, env);
		^MoogFF.ar(Pulse.ar(freq, pw), cutoff.min(16000).max(10), gain);
	}

	// modulated one way
	*square_mod1 {
		arg freq, pw, cutoff, gain, env;
		^MoogFF.ar(Pulse.ar(freq, pw * env), cutoff.min(16000).max(10), gain * env);
	}

	// modulated another way
	*square_mod2 {
		arg freq, pw, cutoff, gain, env;
		^MoogFF.ar(Pulse.ar(freq, pw * env), (cutoff * env).min(16000).max(10), gain);
	}


	// lowpass sine fm
	*sinfmlp {
		arg carhz, modindex, cutoff, modratio, env;
		^LPF.ar(PMOsc.ar(carhz, carhz * modratio, modindex), cutoff);
	}


	// lowpass feedback sine
	*sinfb {
		arg hz1, mod1, hz2, mod2, env;
		^MoogFF.ar(SinOscFB.ar(hz1, mod1*12), hz2, mod2);
	}


	// saturated resonator
	*satrez {
		arg hz1, mod1, hz2, mod2, env;
		var osc = CombC.ar(Impulse.ar(0), delayTime:1/hz1, decayTime:2 ** (mod1 * 100)).distort;
		^MoogFF.ar(osc, hz2, mod2);
	}

	// resonant noises
	*reznoise {
		arg hz1, mod1, hz2, mod2, env;
		var hh = [hz1, hz1];
		var snd = SelectX.ar(mod1, [LFNoise2.ar(hh), LFNoise1.ar(hh), LFNoise0.ar(hh)]);
		^MoogFF.ar(SinOscFB.ar(hz1, mod1*12), hz2, mod2);
	}


	// resonant downsampled dusts
	*rezdust {
		Dust.ar
	}


}