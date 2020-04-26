# frozen_string_literal: true

require 'rack/test'
require 'json'
require_relative '../../app/api'

def app
  ExpenseTracker::API.new
end

module ExpenseTracker
  RSpec.describe 'Expense Tracker Api' do
    include Rack::Test::Methods

    it 'records submited expenses' do
      coffee = {
        'payee' => 'Starbucks',
        'amount' => 5.75,
        'date' => '2019-11-14'
      }

      post '/expenses', JSON.generate(coffee)
    end
  end
end
