Engine_thenbangs : CroneEngine {
	 classvar maxVoices = 16;
	 var server;
	 var pg;
	 var amp=0.3;
	 var release=0.5;
	 var pw=0.5;
	 var cutoff=1000;
	 var gain=2;
	 var pan = 0;

	init {
		this.addCommand("hz", "f", { arg msg;
			var val = msg[1];
			this.playNote(val);
		});

		this.addCommand("amp", "f", { arg msg;
			amp = msg[1];
		});

		this.addCommand("pw", "f", { arg msg;
			pw = msg[1];
		});

		this.addCommand("release", "f", { arg msg;
			release = msg[1];
		});

		this.addCommand("cutoff", "f", { arg msg;
			cutoff = msg[1];
		});

		this.addCommand("gain", "f", { arg msg;
			gain = msg[1];
		});

		this.addCommand("pan", "f", { arg msg;
			postln("pan: " ++ msg[1]);
			pan = msg[1];
		});

		this.addCommand("freeAllNotes", "", {
			// immediately free all currently sustaining notes.
			// this simply ignores any currently sustaining envelopes, so will cause clicks.
			pg.freeAll;
		});

	}
}