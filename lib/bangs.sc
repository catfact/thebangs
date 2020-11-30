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
		arg hz1, mod1, hz2, mod2, env;
		^LPF.ar(PMOsc.ar(hz1, hz1 * mod2, mod1), hz2);
	}


	// lowpass feedback sine
	*sinfb {
		arg hz1, mod1, hz2, mod2, env;
		^MoogFF.ar(SinOscFB.ar(hz1, mod1*4), hz2, mod2);
	}


	// resonant noises
	*reznoise {
		arg hz1, mod1, hz2, mod2, env;
		var hh = [hz1, hz2];
		var snd = SelectX.ar(mod1, [LFNoise2.ar(hh), LFNoise1.ar(hh), LFNoise0.ar(hh)]);
		^MoogFF.ar(SinOscFB.ar(hz1, mod1*12), hh, mod2);
	}



	/// ... to be continued


}