// CroneEngine_PolyPercLimited
// pulse wave with perc envelopes, triggered on freq
// now with voice limit
Thebangs  {

	var server;

	var <>hz1;
	var <>hz2;
	var <>mod1;
	var <>mod2;
	var <>amp;
	var <>pan;
	var <>attack;
	var <>release;

	var <>which; // which bang

	*new {
		^super.new.init;
	}

	init {
		server = Server.default;

		// default parameter values
		hz1 = 330;
		hz2 = 10000;
		mod1 = 0.5;
		mod2 = 0.0;

		which = 0;

		attack = 0.01;
		release = 2;

		amp = 0.1;
		pan = 0.0;
	}

	bang {
		postln("hhellobang");
		{
			arg gate=1;
			var snd, perc, ender;

			perc = EnvGen.ar(Env.perc(attack, release), doneAction:Done.freeSelf);
			ender = EnvGen.ar(Env.asr(0, 1, 0.01), gate:gate, doneAction:Done.freeSelf);

			snd = if (which == 0, {
				Bangs_polyperc.ar(hz1, mod1, hz2, mod2, perc)
			});

			//snd = MoogFF.ar(Pulse.ar(hz1, mod1), hz2, mod2);

			Out.ar(0, Pan2.ar(snd * perc * amp * ender));

		}.play(server);

	}

}