// class to perform simple voice management of 1-shot polyphonic synths.
// assumptions:
// - synths are self-freeing
// - synths have a \gate argument, and setting this to zero will free the synth quickly

OneshotVoicer {
	var <>maxVoices = 32;

	// the voice array
	var <voices;

	// voice stealing mode
	// 0: explicit (steal the oldest plus N)
	// 1: FIFO (steal oldest)
	// 2: LIFO (steal newest)
	// 3: ignore (do nothing until a voice is free)
	var <>stealMode = 1; // FIFO by default

	// explicit voice index to steal
	var <>stealIdx = 0;

	*new { arg maxVoices;
		^super.new.init(maxVoices);
	}

	init { arg mv;
		maxVoices = mv;
		// voice list, oldest-first
		voices = Array.new;
	}

	// request a new note
	// arg f: function returning a Synth which conforms to the assumptions
	newVoice { arg fn;
		var idx, voiceCount;

		// prune all non-running voices
		voices = voices.select({ arg v;
			if (v.isNil, {
				false
			}, {
				v.isPlaying
			});
		});
		voiceCount = voices.size;

		if (voiceCount < maxVoices, {
			// still room to grow: add a voice without stealing
			this.addVoice(fn);
		}, {
			switch(stealMode,
				0, { this.steal(stealIdx, fn) },
				1, { this.steal(0, fn) },
				2, { this.steal(voiceCount-1, fn) },
				3, { /* do nothing */ }
			);
		});
	}

	steal { arg idx, fn;
		var idxClamp = idx.max(0).min(voices.size-1);
		var v = voices[idxClamp];

		if (v.isNil.not, {
			if (v.isPlaying, {
				// cause the stolen voice to stop "real soon"
				v.set(\gate, 0);
			});
			// remove its reference from the books
			voices.removeAt(idxClamp);
		});

		// add a new voice
		this.addVoice(fn);
		//this.printVoiceArray;
	}

	addVoice { arg fn;
		var syn;
		syn = fn.value;
		if (syn.isNil.not, {
			NodeWatcher.register(syn, true);
			voices = voices.add(syn);
		});
	}

	printVoiceArray {
		voices.do({ arg x, i;
			if (x.isNil, { post("!") }, {post(".")});
		});
		postln("")
	}
}