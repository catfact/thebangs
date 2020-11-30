local Thebangs = {}

Thebangs.options = {}
Thebangs.options.algoNames = {
   "square", "square_mod1", "square_mod2",
   "sinfmlp", "sinfb",
   "reznoise",
   "klangha", "klangen"
}

Thebangs.options.stealModes = {
   "static", "FIFO", "LIFO", "ignore"
}

function Thebangs.add_voice_params()
   params:add{
      type="trigger", id="stop_all", name="stop all",
      action=function(value)
	 engine.stopAllVoices()
      end
   }
   
   params:add{
      type="option", id="algo", name="algo", default=1,
      options=Thebangs.options.algoNames,
      action=function(value)
	 engine.algoIndex(value)
      end
   }
   
   params:add{
      type="option", id="steal_mode", name="steal mode", default=2,
      options=Thebangs.options.stealModes,
      action=function(value)
	 engine.stealMode(value-1)
      end
   }
   
   params:add{
      type="number", id="max_voices", name="max voices", min=1, max=32, default=32,
      action=function(value)
	 engine.maxVoices(value)
      end
   }
   
   params:add{
      type="number", id="steal_index", name="steal index", min=0, max=32, default=0,
      action=function(value)
	 engine.stealIndex(value)
      end
   }
   
end

return Thebangs
