// class for generating different ugen subgraphs to stick in thebangs
Bangs_polyperc {
	*ar {
		arg freq, pw, cutoff, gain, env;
		^MoogFF.ar(Pulse.ar(freq, pw), cutoff, gain);
	}
}

Bangs_polyperc_mod1 {
	*ar {
		arg freq, pw, cutoff, gain, env;
		^MoogFF.ar(Pulse.ar(freq, pw * env), cutoff, gain * env);
	}
}



