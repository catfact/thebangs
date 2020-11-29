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
		// the voice list shall be oldest-first
		voices = List.new;
	}

	// request a new note
	// arg f: function returning a Synth which conforms to the assumptions
	newVoice { arg fn;
		var idx;

		postln("\nnew voice...");

		if (voices.size < maxVoices, {
			// still room to grow: add a voice without stealing
			postln("still room to grow; size = "++ voices.size);
			this.addVoice(fn);
		}, {
			idx = this.findFreeIdx;
			if (idx != nil, {
				// found a synth that isn't running, so steal it
				postln("stealing a non-running voice at index: " ++ idx);
				this.steal(idx, fn)
			}, {
				postln("stealing a running voice using steal mode...");
				// all voices are running, so choose one to steal based on current rule
				switch(stealMode,
					0, { this.steal(stealIdx, fn) },
					1, { this.steal(0, fn) },
					2, { this.steal(maxVoices-1, fn) },
					3, { /* do nothing */ }
				);
			});
		});
	}

	findFreeIdx {
		voices.do({ arg x, i;
			if (x.isNil, {
				// shouldn't really happen
				postln("wtf!");
				^i;
			});
			if (x.isRunning.not, {
				^i
			});
		});
		^nil;
	}

	steal { arg idx, fn;
		postln("stealing index: " ++ idx ++ " ; function: " ++ fn);
		if (voices[idx].isNil.not, {
			if (voices[idx].isRunning, {
				// cause the stolen voice to stop "real soon"
				voices[idx].set(\gate, 0);
			});
			// remove its reference from the books
			voices.removeAt(idx);
		});
		// add a new voice
		this.addVoice(fn);
		postln ("new voice count: " ++ voices.size);
		this.printVoiceArray;
	}

	addVoice { arg fn;
		var syn;
		postln("adding new voice");
		syn = fn.value;
		postln("created a synth: " ++ syn);
		if (syn.isNil.not, {
			NodeWatcher.register(syn, true);
			voices.add(syn);
		});
	}

	printVoiceArray {
		voices.do({ arg x, i;
			if (x.isNil, { post("!") }, {post(".")});
		});
		postln("")
	}
}