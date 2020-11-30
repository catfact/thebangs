~bang = Thebangs.new;

~bang.release = 20;

~bang.hz2 = 700;
~bang.mod1=0.6;


~bang.setWhichBang(0);
~bang.setWhichBang(1);
~bang.setWhichBang(2);
~bang.setWhichBang(3);
~bang.setWhichBang(4);
~bang.setWhichBang(5);
~bang.mod2=2;

r = Routine {
	inf.do {
		arg i;
		var hz = 330 * ((i*7)%5 + 1)/((i*9)%7 + 1);
		var t = 0.2 * ((i*15)%5 + 1)/((i*11)%4 + 1);
		//[t, hz].postln;
		~bang.pan = 0.5.rand2;
		~bang.bang(hz);
		t.wait;
	};
}.play;

/*
r.stop;
*/
q = Routine { inf.do {
	~bang.bang((30 + 60.rand).midicps);
	0.1.wait;
} }.play;

q.stop;

~bang.setWhichBang(3);

~bang.mod1=0.1;

~bang.voicer.v.size

~bang.voicer.stealMode = 0

z = Routine { inf.do {
	~bang.voicer.stealIdx = 4.rand;
	1.wait;
} }.play;

~bang.voicer.maxVoices = 4;