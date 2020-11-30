// class to perform simple voice management of 1-shot polyphonic synths.
// assumptions:
// - synths are self-freeing
// - synths have a \gate argument, and setting this to zero will free the synth quickly

OneshotVoicer {
	var <maxVoices = 32;

	// the voice array
	var <voices;

	// voice stealing mode
	// 0: explicit (steal the oldest plus N)
	// 1: FIFO (steal oldest)
	// 2: LIFO (steal newest)
	// 3: ignore (do nothing until a voice slot becomes free)
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

	maxVoices_ { arg max;
		if (max < maxVoices, {
			// if we lower the current count of voices,
			// we should also stop any excess voices currently running
			var activeVoiceCount = this.countActiveVoices;
			while ({activeVoiceCount > max}, {
				this.stealVoice(activeVoiceCount);
				activeVoiceCount = this.countActiveVoices;
			});
		});
		maxVoices = max;
	}

	// count active voices, pruning references to inactive ones from the list
	countActiveVoices {
		// prune all non-running voices
		voices = voices.select({ arg v;
			if (v.isNil, {
				false
			}, {
				v.isPlaying
			});
		});
		^voices.size;
	}

	// request a new voice to be added
	// arg f: function returning a Synth which conforms to the assumptions
	newVoice { arg fn;
		var activeVoiceCount;
		
		activeVoiceCount = this.countActiveVoices;

		if (activeVoiceCount < maxVoices, {
			// still room to grow: add a voice without stealing
			this.addVoice(fn);
		}, {
			this.stealVoice(activeVoiceCount);
			this.addVoice(fn);
		});
	}

	// steal the next running voice according to the current heuristic
	stealVoice { arg activeVoiceCount;		
		switch(stealMode,
			0, { this.stealVoiceIdx(stealIdx) },
			1, { this.stealVoiceIdx(0) },
			2, { this.stealVoiceIdx(activeVoiceCount-1) },
			3, { /* do nothing */ }
		);
	}

	// steal a specific voice by index
	stealVoiceIdx { arg idx;
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
	}

	// add a voice, given a Synth-creating function
	addVoice { arg fn;
		var syn;
		syn = fn.value;
		if (syn.isNil.not, {
			NodeWatcher.register(syn, true);
			voices = voices.add(syn);
		});
	}

}